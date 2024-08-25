FROM gradle:8.7.0-jdk21-alpine AS builder
USER gradle
WORKDIR /home/cyb3rhq-indexer
COPY --chown=gradle:gradle . /home/cyb3rhq-indexer
RUN gradle clean


FROM eclipse-temurin:21-jdk-alpine
RUN apk add git && \
  apk add curl && \
  apk add bash && \
  addgroup -g 1000 cyb3rhq-indexer && \
  adduser -u 1000 -G cyb3rhq-indexer -D -h /home/cyb3rhq-indexer cyb3rhq-indexer && \
  chmod 0775 /home/cyb3rhq-indexer && \
  chown -R 1000:0 /home/cyb3rhq-indexer
USER cyb3rhq-indexer
COPY --from=builder --chown=1000:0 /home/cyb3rhq-indexer /home/cyb3rhq-indexer
WORKDIR /home/cyb3rhq-indexer
RUN git config --global --add safe.directory /home/cyb3rhq-indexer
EXPOSE 9200 9300
