# Cyb3rhq to Splunk Integration Developer Guide

This document describes how to prepare a Docker Compose environment to test the integration between Cyb3rhq and Splunk. For a detailed guide on how to integrate Cyb3rhq with Splunk, please refer to the [Cyb3rhq documentation](https://documentation.wazuh.com/current/integrations-guide/splunk/index.html).

## Requirements

- Docker and Docker Compose installed.

## Usage

1. Clone the Cyb3rhq repository and navigate to the `integrations/` folder.
2. Run the following command to start the environment:
   ```bash
   docker compose -f ./docker/compose.indexer-splunk.yml up -d
   ```
3. If you prefer, you can start the integration with the Cyb3rhq Manager as data source:
   ```bash
   docker compose -f ./docker/compose.manager-splunk.yml up -d
   ```

The Docker Compose project will bring up the following services:

- 1x Events Generator (learn more in [cyb3rhq-indexer/integrations/tools/events-generator](../tools/events-generator/README.md)).
- 1x Cyb3rhq Indexer (OpenSearch).
- 1x Logstash
- 1x Splunk
- 1x Cyb3rhq Manager (optional).

For custom configurations, you may need to modify these files:

- [docker/compose.indexer-splunk.yml](../docker/compose.indexer-splunk.yml): Docker Compose file.
- [docker/.env](../docker/.env): Environment variables file.
- [splunk/logstash/pipeline/indexer-to-splunk.conf](./logstash/pipeline/indexer-to-splunk.conf): Logstash Pipeline configuration file.

If you opted to start the integration with the Cyb3rhq Manager, you can modify the following files:

- [docker/compose.manager-splunk.yml](../docker/compose.manager-splunk.yml): Docker Compose file.
- [splunk/logstash/pipeline/manager-to-splunk.conf](./logstash/pipeline/manager-to-splunk.conf): Logstash Pipeline configuration file.

Check the files above for **credentials**, ports, and other configurations.

| Service       | Address                | Credentials         |
| ------------- | ---------------------- | ------------------- |
| Cyb3rhq Indexer | https://localhost:9200 | admin:admin         |
| Splunk        | https://localhost:8000 | admin:Password.1234 |

## Importing the dashboards

The dashboards for Splunk are included in this folder. The steps to import them to Splunk are the following:

- In the Splunk UI, go to `Settings` > `Data Inputs` > `HTTP Event Collector` and make sure that the `hec` token is enabled and uses the `cyb3rhq-alerts` index.
- Open a dashboard file and copy all its content.
- In the Splunk UI, navigate to `Search & Reporting`, `Dashboards`, click `Create New Dashboard`, write the title and select `Dashboard Studio`, select `Grid` and click on `Create`.
- On the top menu, there is a `Source` icon. Click on it, and replace all the content with the copied content from the dashboard file. After that, click on `Back` and click on `Save`.
- Repeat the steps for all the desired dashboards.

Imported dashboards will appear under `Search & Reporting` > `Dashboards`.
