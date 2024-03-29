#include "util.h"
#include "counter.h"
#include "com.h"
#include "protocol.h"
#include <stdio.h>
#include <stdlib.h>
#include <iostream.h>
#pragma comment( lib, "wsock32.lib" )     // This will link to wsock32.lib

#define LINEMAX 10
#define SHOW 1
#define HIDE 0
#define USB_MODE 'u'
#define ETH_MODE 'e'

#define ENC_MODE 'c'
#define DEC_MODE 'd'

#define N 2000
#define INC 20

#define MODE FILE
packet_t packet;

int main(int argc, char*argv[])
{

	int XORorBOX = 0;
	char src_file[100],c;
	char dest_file[100];

	FILE * fp_r,*fp_w;
	int sz;
	char com;
	double time,bsent;


	u32 mode;
	u32 version=1;
	u32 data_len,n;
	u32 inc = 20;

	u8 *Key;
	u32 origkey[8];
	u8 IV[32]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	u8 tv[16]={0x33,0x22,0x11,0x00,0x77,0x66,0x55,0x44,0xBB,0xAA,0x99,0x88,0xFF,0xEE,0xDD,0xCC};
	u8 *data_in;
	u8 buffer_in[MAX_DATA_IN];
	u8 buffer_out[MAX_DATA_OUT];
	u32 size_out,i,m,j;

	size_out = 0;
	version = 1;
	mode = ROUNDS_10 | CBC_FLAG |FIRST_FLAG| ENCRYPT_FLAG;

		if( argc<2 )
		{
			printf("\nUsage: Demo -mode src_file dest_file \n \n__-mode parameters__ \n\t -c: cypher mode\n\t -d: decryption mode \n");
			return -1;
		}

		if(argv[1][0]='-')
		{
			if(argv[1][1]=='c')
				mode = ROUNDS_10 | CBC_FLAG |FIRST_FLAG| ENCRYPT_FLAG;
			else if(argv[1][1]=='d')
				mode = ROUNDS_10 | CBC_FLAG |FIRST_FLAG | DECRYPT_FLAG;
			else
			{
				printf("\nUsage: Demo -mode src_file dest_file \n \n__-mode parameters__ \n\t -c: cypher mode\n\t -d: decryption mode \n");
				return -1;
			}
		}


		size_out = 0;
		version = 1;

		//strcpy(src_file,argv[2]);
		//strcpy(dest_file,argv[3]);

		// APENAS FAZ O XOR

		if(XORorBOX==0){


			char buffer_in[256];
			char buffer_out[256];
   			char key[10]="ABCDERTYUI";
   			char chave = '0';
			int x;
			int count;

   			//gets(s);

   			//strcat(s, "e um dia chegou lá");
   				//for(x=0; x<2048; x++){
   					//s[x]=s[x]^key[x];
					//s[x]=s[x]^chave;
					//count<<s[x];
   				//}


   			//fclose(fp_r);
   			//fputs( s, stdout);
   			//fwrite(s,1,2048,stdout)

			while((data_len=fread(buffer_in,1,256,stdin))>0)
			{

				for(x=0; x<data_len; x++){
					buffer_out[x]=buffer_in[x]^chave;
				}

				fwrite(buffer_out,1,data_len,stdout);
			}


		}

		// CODIGO DO PROFESSOR

		else{

			init( mode);

			StartCounter();
			while( (data_len = fread(buffer_in,1,MAX_DATA_IN,stdin))>0)
			{
				if(data_len == ERROR_CODE)
				{
					printf("\n Error : fread() \n");
					return -1;
				}

				if(update(buffer_in,data_len,buffer_out,&n)== ERROR_CODE)
				{
					printf("\n Error : Update() \n");
					return -1;
				}

				fwrite(buffer_out,1,n,stdout);
			}

			if(doFinal(buffer_out,&n) == ERROR_CODE)
				{
					printf("\n Error : Update() \n");
					return -1;
				}
			if(n>0)
				fwrite(buffer_out,1,n,stdout);

			time = GetCounter()*((double)1000); // in useconds
			bsent = (double) 8*sz; // bytes to bits
			//printf("time: %f us \n bit rate: %f Mb/s \n",time, (bsent/time));

		}

}
