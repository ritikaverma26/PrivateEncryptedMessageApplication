# PrivateEncryptedMessageApplication
This is an android app which uses the cryptography libraries in Java/Android Studio to encrypt messages by implementing encryption algorithms(DES,AES,RSA) and sends it on any messaging platform.
It consists of MainActivity.java which is the parent activity and has three activities for each of the three encryption algorithms.
For the DES and AES encryption/decryption only a single key is generated using the KeySpec class and SecretKeyFactory class.
For the asymmetric encryption algorithm, RSA, we have generated a pair of keys using the KeyPairGenerator class.
