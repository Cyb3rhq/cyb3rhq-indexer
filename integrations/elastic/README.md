# Cyb3rhq to Elastic Integration Developer Guide

This document describes how to prepare a Docker Compose environment to test the integration between Cyb3rhq and the Elastic Stack. For a detailed guide on how to integrate Cyb3rhq with Elastic Stack, please refer to the [Cyb3rhq documentation](https://documentation.wazuh.com/current/integrations-guide/elastic-stack/index.html).

## Requirements

- Docker and Docker Compose installed.

## Usage

1. Clone the Cyb3rhq repository and navigate to the `integrations/` folder.
2. Run the following command to start the environment:
   ```bash
   docker compose -f ./docker/compose.indexer-elastic.yml up -d
   ```
3. If you prefer, you can start the integration with the Cyb3rhq Manager as data source:
   ```bash
   docker compose -f ./docker/compose.manager-elastic.yml up -d
   ```

The Docker Compose project will bring up the following services:

- 1x Events Generator (learn more in [cyb3rhq-indexer/integrations/tools/events-generator](../tools/events-generator/README.md)).
- 1x Cyb3rhq Indexer (OpenSearch).
- 1x Logstash
- 1x Elastic
- 1x Kibana
- 1x Cyb3rhq Manager (optional).

For custom configurations, you may need to modify these files:

- [docker/compose.indexer-elastic.yml](../docker/compose.indexer-elastic.yml): Docker Compose file.
- [docker/.env](../docker/.env): Environment variables file.
- [elastic/logstash/pipeline/indexer-to-elastic.conf](./logstash/pipeline/indexer-to-elastic.conf): Logstash Pipeline configuration file.

If you opted to start the integration with the Cyb3rhq Manager, you can modify the following files:

- [docker/compose.manager-elastic.yml](../docker/compose.manager-elastic.yml): Docker Compose file.
- [elastic/logstash/pipeline/manager-to-elastic.conf](./logstash/pipeline/manager-to-elastic.conf): Logstash Pipeline configuration file.

Check the files above for **credentials**, ports, and other configurations.

| Service       | Address                | Credentials     |
| ------------- | ---------------------- | --------------- |
| Cyb3rhq Indexer | https://localhost:9200 | admin:admin     |
| Elastic       | https://localhost:9201 | elastic:elastic |
| Kibana        | https://localhost:5602 | elastic:elastic |

## Importing the dashboards

The dashboards for Elastic are included in [dashboards.ndjson](./dashboards.ndjson). The steps to import them to Elastic are the following:

- On Kibana, expand the left menu, and go to `Stack management`.
- Click on `Saved Objects`, select `Import`, click on the `Import` icon and browse the dashboard file.
- Click on Import and complete the process.

Imported dashboards will appear in the `Dashboards` app on the left menu.
