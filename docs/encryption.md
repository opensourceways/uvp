# Protect Sensitive Data

UVP utilizes [Jasypt](http://www.jasypt.org/) Password-Based Encryption (PBE) tools to
encrypt sensitive data, such as tokens.

The encryption password is recommended to be passed to UVP as environment variable
(see [deployment](deployment.md) for details).

## Encryption

After UVP starts up, you can post `{host}/encrypt-api/encrypt` with raw message as `text` parameter
to encrypt your sensitive data.

```curl -X POST -F text=your_sensitive_data {host}/encrypt-api/encrypt```

## Decryption

Ciphertexts encrypted with the same encryption password can be decrypted when UVP try to use them.
For example, when UVP calls OSS Index API, it will try to get the tokens. 

---
[Back to the README](../README.md)
