#!/bin/bash

# Cyb3rhq Docker Copyright (C) 2017, Cyb3rhq Inc. (License GPLv2)
# This has to be exported to make some magic below work.
export DH_OPTIONS

export NAME=cyb3rhq-indexer
export TARGET_DIR=${CURDIR}/debian/${NAME}

# Package build options
export LOG_DIR=/var/log/${NAME}
export LIB_DIR=/var/lib/${NAME}
export PID_DIR=/run/${NAME}
export INDEXER_HOME=/usr/share/${NAME}
export CONFIG_DIR=${INDEXER_HOME}/config
export BASE_DIR=${NAME}-*

rm -rf ${INDEXER_HOME:?}/
tar -xf "${INDEXER_TAR_NAME}"

## TOOLS

## Variables
TOOLS_PATH=${NAME}-${VERSION}/plugins/opensearch-security/tools
CERT_TOOL=${TOOLS_PATH}/cyb3rhq-certs-tool.sh

# generate certificates
cp $CERT_TOOL .
chmod 755 cyb3rhq-certs-tool.sh && bash cyb3rhq-certs-tool.sh -A

# copy to target
mkdir -p ${TARGET_DIR}${INDEXER_HOME}
# mkdir -p ${TARGET_DIR}${INDEXER_HOME}/opensearch-security/ <-- empty dir
mkdir -p ${TARGET_DIR}${CONFIG_DIR}
mkdir -p ${TARGET_DIR}${LIB_DIR}
mkdir -p ${TARGET_DIR}${LOG_DIR}
mkdir -p ${TARGET_DIR}/etc/init.d
mkdir -p ${TARGET_DIR}/etc/default
mkdir -p ${TARGET_DIR}/usr/lib/tmpfiles.d
mkdir -p ${TARGET_DIR}/usr/lib/sysctl.d
mkdir -p ${TARGET_DIR}/usr/lib/systemd/system
mkdir -p ${TARGET_DIR}${CONFIG_DIR}/certs
# Copy installation files to final location
cp -pr ${BASE_DIR}/* ${TARGET_DIR}${INDEXER_HOME}
cp -pr /opensearch.yml ${TARGET_DIR}${CONFIG_DIR}
# Copy Cyb3rhq indexer's certificates
cp -pr /cyb3rhq-certificates/demo.indexer.pem ${TARGET_DIR}${CONFIG_DIR}/certs/indexer.pem
cp -pr /cyb3rhq-certificates/demo.indexer-key.pem ${TARGET_DIR}${CONFIG_DIR}/certs/indexer-key.pem
cp -pr /cyb3rhq-certificates/root-ca.key ${TARGET_DIR}${CONFIG_DIR}/certs/root-ca.key
cp -pr /cyb3rhq-certificates/root-ca.pem ${TARGET_DIR}${CONFIG_DIR}/certs/root-ca.pem
cp -pr /cyb3rhq-certificates/admin.pem ${TARGET_DIR}${CONFIG_DIR}/certs/admin.pem
cp -pr /cyb3rhq-certificates/admin-key.pem ${TARGET_DIR}${CONFIG_DIR}/certs/admin-key.pem

# Set path to indexer home directory
sed -i 's/-Djava.security.policy=file:\/\/\/etc\/cyb3rhq-indexer\/opensearch-performance-analyzer\/opensearch_security.policy/-Djava.security.policy=file:\/\/\/usr\/share\/cyb3rhq-indexer\/opensearch-performance-analyzer\/opensearch_security.policy/g' ${TARGET_DIR}${CONFIG_DIR}/jvm.options

chmod -R 500 ${TARGET_DIR}${CONFIG_DIR}/certs
chmod -R 400 ${TARGET_DIR}${CONFIG_DIR}/certs/*

find ${TARGET_DIR} -type d -exec chmod 750 {} \;
find ${TARGET_DIR} -type f -perm 644 -exec chmod 640 {} \;
find ${TARGET_DIR} -type f -perm 664 -exec chmod 660 {} \;
find ${TARGET_DIR} -type f -perm 755 -exec chmod 750 {} \;
find ${TARGET_DIR} -type f -perm 744 -exec chmod 740 {} \;
