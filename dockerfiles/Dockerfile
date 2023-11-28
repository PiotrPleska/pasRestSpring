FROM mongo:6-jammy

# Copy your JavaScript script into the container
COPY initReplicaSet.js /scripts/
RUN chmod 777 /scripts/initReplicaSet.js

# Copy the keyFile to the correct location
COPY --chown=999 --chmod=999 keyFile /etc/mongo/keyFile
RUN chmod 400 /etc/mongo/keyFile

# Copy the mongod.conf file to the correct location
COPY --chown=999 --chmod=999 mongod.conf /etc/mongod.conf

# Use the custom configuration file
CMD ["mongod", "--config", "/etc/mongod.conf"]
