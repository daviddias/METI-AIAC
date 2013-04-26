package aiac.ptcc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.UUID;

import org.apache.log4j.Logger;

import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.CK_C_INITIALIZE_ARGS;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.CK_SESSION_INFO;
import sun.security.pkcs11.wrapper.PKCS11;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;


/**
 * This class contains support methods for Authentication Service and Agent
 * 
 * @author DavidDias
 *
 */
public class Utils {

	private static  PKCS11 pkcs11;
	static Logger logger = Logger.getLogger(Utils.class);
	

	public static byte[] ReadFileToByteArray(String filename) throws IOException {
		File file = new File(filename);
		FileInputStream file_input = new FileInputStream (file);
		DataInputStream data_in    = new DataInputStream (file_input);
		byte[] data = new byte[(int)file.length()];
		data_in.read(data);
		data_in.close();
		return data;
	}


	public static void WriteDataToFile(String filename, byte[] data) throws IOException {
		File file = new File (filename);
		FileOutputStream file_output = new FileOutputStream (file);
		DataOutputStream data_out = new DataOutputStream (file_output);
		data_out.write(data);
		file_output.close();
	}


	public static PublicKey ReadKeyFromFile(String filename) throws NoSuchAlgorithmException, InvalidKeySpecException {
		logger.debug("Going to Read Public Key from file: " + filename);
		byte[] encodedPublicKey = null;
		try { encodedPublicKey = ReadFileToByteArray(filename); } 
		catch(IOException e) { logger.debug("Signature File doesn't exist"); }
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
		PublicKey pkey = keyFactory.generatePublic(publicKeySpec);
		return pkey;
	}


	public static void WriteKeytoFile(PublicKey key, String filename) throws IOException {
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key.getEncoded());
		FileOutputStream fout = new FileOutputStream(filename);
		fout.write(x509EncodedKeySpec.getEncoded());
		fout.close();
	}


	public static byte[] Generate128bitNounce()
	{
		UUID uuid = UUID.randomUUID();
		byte[] nounce = uuid.toString().getBytes();
		return nounce;
	}


	static byte[] ToBytes(BigInteger bi, int size)
	{
		byte[] b = bi.toByteArray();
		if (b.length == size)
			return b;
		else if (b.length != size + 1) {
			System.out.println("length = " + b.length + " instead of " + size);
			return null;
		}
		byte[] r = new byte[size];
		System.arraycopy(b, 1, r, 0, size);
		return r;
	}


	static String csCvcCertRole01 = "7F2181CD5F37818086B81455BCF0F508CF79FE9CAD574CA631EBC392D78869C4DC29DB193D75AC7E1BB1852AA57FA54C7E7FA97CBB536F2FA384C90C4FF62EAB119156016353AEAFD0F2E2B41BF89CCFE2C5F463A4A30DC38F2B9145DA3F12C40E2F394E7EE606A4C9377253D6E46D7B538B34C712B964F4A20A5724E0F6E88E0D5D1188C39B75A85F383C6917D61A07CFF92106D1885E393F68BA863520887168CE884242ED86F2F80397B42B883D931F8CCB141DC3579E5AB798B8CCF9A189B83B8D0001000142085054474F56101106"; // CVC cert for writing
	static String csCvcCertRole02 = "7F2181CD5F3781804823ED79D2F59E61E842ABE0A58919E63F362C9133E873CA77DD79AD01009247460DFE0294DD0ABAABE1D262E69A165F2F1AC6E953E8ABBE3BF1D2ACD6EB69EE83AB918D6F5116589BE0D40E780D5635238B78AA4290AD32F2A6316D24B417E06591DE6A775C38CFD918CA4FD11146EA20E06FE7F73CA7B3D3058FA259745D875F383C6917D61A07CFF92106D1885E393F68BA863520887168CE884242ED86F2F80397B42B883D931F8CCB141DC3579E5AB798B8CCF9A189B83B8D0001000142085054474F56101106"; // CVC cert for reading
	static String csCvcMod = "924557F6E1C2F1898B391D9255CC72FD7F11128BA148CFEBD1F58AF3F363778157E262FD72A76BCCA0AB43D8F5272E00D21B8B0EE4CC7DA86C8189DEC0DDC58C6A54A81BCE5E52076917D61A07CFF92106D1885E393F68BA863520887168CE884242ED86F2F80397B42B883D931F8CCB141DC3579E5AB798B8CCF9A189B83B8D"; // private key modulus
	static String csCvcExp = "3B35A8CAFE4E6C79D20AB7C6C1C67611D97AEEB7E8FCD175D353030187578F4BA368B7CB82BAF4EF2B66C89B2D79C3AC7F60B8E4B98771A258F202FE51B23441EB29C68569B608EF1F4B3CF15C68744AA7A3800E364739D3C6DCB078EFB81EA3197C843EE17BD9BCF1E0FEB4FFB6719F923C63105206A2F5A77A0437D762E781"; // private key exponent

	public byte[] SignChallenge(byte[] challenge)
	{
		BigInteger mod = new BigInteger("00" + csCvcMod, 16);
		BigInteger exp = new BigInteger("00" + csCvcExp, 16);
		BigInteger chall = new BigInteger(1, challenge);

		BigInteger signat = chall.modPow(exp, mod);

		return ToBytes(signat, 128);
	}


	private static byte[] makeBA(int len, byte val)
	{
		byte ret[] = new byte[len];
		for (int i = 0; i < len; i++)
			ret[i] = val;
		return ret;
	}


	private static char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };	
	public static String ToHex(byte[] ba)
	{
		StringBuffer buf = new StringBuffer(3 * ba.length + 2);
		for (int i = 0; i < ba.length; i++)
		{
			int c = ba[i];
			if (c < 0)
				c += 256;
			buf.append(HEX[c / 16]);
			buf.append(HEX[c % 16]);
			buf.append(' ');
		}

		return new String(buf);
	}

}
