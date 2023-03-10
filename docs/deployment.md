# Deployment

## Prerequisites

1. UVP relies on a [PostgreSQL](https://www.postgresql.org/) database,
   install and start up one before deploying UVP.
2. UVP can call [NVD CVE API](https://nvd.nist.gov/developers/vulnerabilities) to retrieve CVEs,
   it is recommended to [configure an NVD API key](https://nvd.nist.gov/developers/request-an-api-key).
3. UVP can call [OSS Index API](https://ossindex.sonatype.org/rest) to query vulnerabilities,
   you should configure at least one API token, and it is recommended to configure multiple tokens.

## Deploy UVP with Docker

Database configures, such as *host/port/db_name* are passed to the container via
environment variables.

Sensitive data, including *database password/Jasypt encryption password* (see [encryption](encryption.md) for details),
are passed by *docker secret*.

1. `docker swarm init`
2. `printf {your_db_user_password} | docker secret create db_password -`
3. `printf {your_encryption_password} | docker secret create encryption_password -`
4. `docker build . -t uvp`
5. ```
    docker service create 
    --secret="db_password" \
    --secret="encryption_password" \
    --publish published={your_host_port},target={your_container_port} \
    -e DB_HOST="{your_db_host}" \
    -e DB_PORT="{your_db_port}" \
    -e DB_NAME="{your_db_name}" \
    -e DB_USERNAME="{your_db_username}" \
    -e DB_PASSWORD_FILE="/run/secrets/db_password" \
    -e ENCRYPTION_PASSWORD_FILE="/run/secrets/encryption_password" \
    uvp
    ```

---
[Back to the README](../README.md)
