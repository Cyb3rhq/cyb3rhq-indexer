# Cyb3rhq Docker Copyright (C) 2017, Cyb3rhq Inc. (License GPLv2)
FROM amazonlinux:2023.3.20240219.0 AS builder

ARG VERSION
ARG INDEXER_TAR_NAME

RUN yum install openssl tar findutils shadow-utils -y

COPY ${INDEXER_TAR_NAME} /

COPY config/opensearch.yml /

COPY config/config.yml /

COPY config/config.sh /

RUN bash config.sh

################################################################################
# Build stage 1 (the actual Cyb3rhq indexer image):
#
# Copy cyb3rhq-indexer from stage 0
# Add entrypoint

################################################################################
FROM amazonlinux:2023.3.20240219.0

ENV USER="cyb3rhq-indexer" \
    GROUP="cyb3rhq-indexer" \
    NAME="cyb3rhq-indexer" \
    INSTALL_DIR="/usr/share/cyb3rhq-indexer"

RUN yum install curl-minimal shadow-utils findutils hostname -y

RUN getent group $GROUP || groupadd -r -g 1000 $GROUP

RUN useradd --system \
    --uid 1000 \
    --no-create-home \
    --home-dir $INSTALL_DIR \
    --gid $GROUP \
    --shell /sbin/nologin \
    --comment "$USER user" \
    $USER

WORKDIR $INSTALL_DIR

COPY entrypoint.sh /

COPY config/securityadmin.sh /

RUN chmod 700 /entrypoint.sh && chmod 700 /securityadmin.sh

RUN chown 1000:1000 /*.sh

COPY --from=builder --chown=1000:1000 /debian/cyb3rhq-indexer/usr/share/cyb3rhq-indexer /usr/share/cyb3rhq-indexer
COPY --from=builder --chown=0:0 /debian/cyb3rhq-indexer/usr/lib/systemd /usr/lib/systemd
COPY --from=builder --chown=0:0 /debian/cyb3rhq-indexer/usr/lib/sysctl.d /usr/lib/sysctl.d
COPY --from=builder --chown=0:0 /debian/cyb3rhq-indexer/usr/lib/tmpfiles.d /usr/lib/tmpfiles.d

RUN chown -R 1000:1000 /usr/share/cyb3rhq-indexer

RUN mkdir -p /var/lib/cyb3rhq-indexer && chown 1000:1000 /var/lib/cyb3rhq-indexer && \
    mkdir -p /usr/share/cyb3rhq-indexer/logs && chown 1000:1000 /usr/share/cyb3rhq-indexer/logs && \
    mkdir -p /run/cyb3rhq-indexer && chown 1000:1000 /run/cyb3rhq-indexer && \
    mkdir -p /var/log/cyb3rhq-indexer && chown 1000:1000 /var/log/cyb3rhq-indexer && \
    chmod 700 /usr/share/cyb3rhq-indexer && \
    chmod 600 /usr/share/cyb3rhq-indexer/config/jvm.options && \
    chmod 600 /usr/share/cyb3rhq-indexer/config/opensearch.yml

USER cyb3rhq-indexer

# Services ports
EXPOSE 9200

ENTRYPOINT ["/entrypoint.sh"]
# Dummy overridable parameter parsed by entrypoint
CMD ["opensearchwrapper"]