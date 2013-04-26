package aiac.ptcc;

import java.io.IOException;
import java.lang.reflect.Method;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import pteidlib.PTEID_Certif;
import pteidlib.PteidException;
import pteidlib.pteid;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.CK_C_INITIALIZE_ARGS;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.CK_SLOT_INFO;
import sun.security.pkcs11.wrapper.PKCS11;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;

/**
 * This class contains the API to use Portuguese Citizen Card Connect to Card Reader
 * 
 * @author DavidDias
 *
 */
public class CCAPI {

	static Logger logger = Logger.getLogger(CCAPI.class);

	private static  PKCS11 pkcs11;

	static {
		logger.debug("Initializing of CCAPI - Start: " + System.getProperty("java.library.path"));
		try { System.loadLibrary("pteidlibj");
		} catch (UnsatisfiedLinkError e) {
			logger.debug("Native code library failed to load.\n" + e);
			System.exit(1);
		}
		logger.debug("Initializing of CCAPI - Complete");
	}


	public static void initialize() throws PteidException {
		logger.debug("Initializing pteidlib - Start");
		pteidlib.pteid.Init("");	
		pteid.SetSODChecking(false);
		int cardType = pteid.GetCardType();
		
		switch (cardType) {
		case pteid.CARD_TYPE_IAS101: logger.debug("Type: IAS 1.0.1"); break;
		case pteid.CARD_TYPE_IAS07: logger.debug("Type: IAS 7"); break;
		case pteid.CARD_TYPE_ERR: logger.debug("Error reading card"); break;
		default: logger.debug("Unknow card type"); break;
		}
		logger.debug("Initializing pteidlib - Complete");
	}


	public static X509Certificate getCertificateX509() throws PteidException {
		PTEID_Certif[] certs = pteid.GetCertificates();
		try {
			X509Certificate cert = X509Certificate.getInstance(certs[0].certif);
			return cert;
		} catch (CertificateException e) { e.printStackTrace(); }
		return null;
	}


	//returns the p11_session
	public static long PKCS11SessionInit() throws IOException, PKCS11Exception {
		String osName = System.getProperty("os.name");
		String javaVersion = System.getProperty("java.version");
		long p11_session = 0;

		try {
			String libName = "libbeidpkcs11.so"; // For Linux
			if (-1 != osName.indexOf("Windows")){
				logger.debug("Working on Windows");
				libName = "pteidpkcs11.dll";
			}
			else if (-1 != osName.indexOf("Mac")){
				logger.debug("Working on a Mac");
				libName = "pteidpkcs11.dylib";
			}else { logger.debug("Working on a Linux"); }
			
			Class pkcs11Class = Class.forName("sun.security.pkcs11.wrapper.PKCS11");

			if (javaVersion.startsWith("1.5.")) {
				Method getInstanceMethod = pkcs11Class.getDeclaredMethod("getInstance", new Class[] { String.class, CK_C_INITIALIZE_ARGS.class, boolean.class });
				pkcs11 = (PKCS11)getInstanceMethod.invoke(null, new Object[] { libName, null, false });
			}
			else {	
				Method getInstanceMethod = pkcs11Class.getDeclaredMethod("getInstance", new Class[] { String.class, String.class, CK_C_INITIALIZE_ARGS.class, boolean.class });
				pkcs11 = (PKCS11)getInstanceMethod.invoke(null, new Object[] { libName, "C_GetFunctionList", null, false });
			}
			
			p11_session = pkcs11.C_OpenSession(0, PKCS11Constants.CKF_SERIAL_SESSION, null, null);
			logger.debug("p11_session opened");
			
			CK_SLOT_INFO info =	pkcs11.C_GetSlotInfo(p11_session);
			logger.debug("CK SLOT INFO " + info.toString());
		}  catch (Exception e) { logger.debug("[Catch] Exception: " + e.getMessage()); }
		return p11_session;
	}

	public static byte[] SignNounce(long p11_session, byte[] nounce) throws PKCS11Exception, IOException {
		CK_ATTRIBUTE[] attributes = new CK_ATTRIBUTE[1];
		attributes[0] = new CK_ATTRIBUTE();
		attributes[0].type = PKCS11Constants.CKA_LABEL;
		attributes[0].pValue = "CITIZEN AUTHENTICATION KEY";

		pkcs11.C_FindObjectsInit(p11_session, attributes);
		
		long[] keyHandles = pkcs11.C_FindObjects(p11_session, 1);
		long signatureKey = keyHandles[0];

		pkcs11.C_FindObjectsFinal(p11_session);

		//Initialize the signature
		CK_MECHANISM mechanism = new CK_MECHANISM();
		mechanism.mechanism = PKCS11Constants.CKM_SHA1_RSA_PKCS;
		mechanism.pParameter = null;

		pkcs11.C_SignInit(p11_session, mechanism, signatureKey);

		//Sign the data
		byte[] signature = pkcs11.C_Sign(p11_session, nounce);

		logger.debug("The Nounce is Signed =)");
		return signature;
	}

	public static boolean validateSig(PublicKey publicKey, byte[] signature, byte[] nounce){
		Signature sig = null;
		try { sig = Signature.getInstance("SHA1withRSA"); } catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
		String key = "PublicKey.pub";
		try { sig.initVerify(publicKey); } catch (InvalidKeyException e) { e.printStackTrace(); }
		try { sig.update(nounce); } catch (SignatureException e) { e.printStackTrace(); }      
		boolean verificationResult = false;
		try { verificationResult = sig.verify(signature); } catch (SignatureException e) { e.printStackTrace(); }
		logger.debug("Is the signature valid? -> " + verificationResult);
		return verificationResult;
	}

	/**
	 * ------------------------------------------------------------------------------------
	 * 					Testing Methods to check if everything is Ok with Card
	 * ------------------------------------------------------------------------------------
	 */

	/**
	 * Main for Testing purposes
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			PropertyConfigurator.configure("log4j.properties");
			logger.debug("TESTING LOGGER");
			initialize();
			//System.out.println("-----------\n");
			//System.out.println(getCertificateX509().toString());
			//System.out.println("-----------\n");

			logger.debug("\n\n Let's TEST this =D\n");
			byte[] nounce = Utils.Generate128bitNounce();
			long p11_session = PKCS11SessionInit();
			byte[] signature = SignNounce(p11_session,nounce);
			validateSig(getCertificateX509().getPublicKey(),signature,nounce);
			logger.debug("\n Voilá! \n");

			pteid.Exit(pteid.PTEID_EXIT_LEAVE_CARD);
		} catch (Exception e) { e.printStackTrace();}
	}













}
