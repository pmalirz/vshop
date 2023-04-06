FROM mongo:latest
HEALTHCHECK NONE
COPY replica.key /data/replica.key.dev
COPY ./scripts /docker-entrypoint-initdb.d
RUN cp /data/replica.key.dev /data/replica.key
RUN chmod 400 /data/replica.key
RUN chown 999:999 /data/replica.key
CMD ["--replSet", "rs0", "--keyFile", "/data/replica.key", "--bind_ip_all", "--auth"]