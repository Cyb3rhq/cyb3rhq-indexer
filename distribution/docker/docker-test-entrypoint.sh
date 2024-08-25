#!/usr/bin/env bash
set -e -o pipefail

cd /usr/share/cyb3rhq-indexer/bin/

/usr/local/bin/docker-entrypoint.sh | tee > /usr/share/cyb3rhq-indexer/logs/console.log
