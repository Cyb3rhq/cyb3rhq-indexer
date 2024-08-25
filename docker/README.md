# Indexer development environments

Install [Docker Desktop][docker-desktop] as per its instructions, available for Windows, Mac
and Linux (Ubuntu, Debian & Fedora).
This ensures that the development experience between Linux, Mac and Windows is as
similar as possible.

> IMPORTANT: be methodic during the installation of Docker Desktop, and proceed
> step by step as described in their documentation. Make sure that your system
> meets the system requirements before installing Docker Desktop, and read any
> post-installation note, specially on Linux: [Differences between
> Docker Desktop for Linux and Docker Engine][docker-variant].

## Pre-requisites

1. Assign resources to [Docker Desktop][docker-desktop]. The requirements for the
   environments are:

   - 8 GB of RAM (minimum)
   - 4 cores

   The more resources the better ☺

2. Clone the [cyb3rhq-indexer][wi-repo].

3. Set up user permissions

   The Docker volumes will be created by the internal Docker user, making them
   read-only. To prevent this, a new group named `docker-desktop` and GUID 100999
   needs to be created, then added to your user and the source code folder:

   ```bash
   sudo groupadd -g 100999 docker-desktop
   sudo useradd -u 100999 -g 100999 -M docker-desktop
   sudo chown -R docker-desktop:docker-desktop $WZD_HOME
   sudo usermod -aG docker-desktop $USER
   ```

## Understanding Docker contexts

Before we begin starting Docker containers, we need to understand the
differences between Docker Engine and Docker Desktop, more precisely, that the
use different contexts.

Carefully read these two sections of the Docker documentation:

- [Differences between Docker Desktop for Linux and Docker Engine][docker-variant].
- [Switch between Docker Desktop and Docker Engine][docker-context].

Docker Desktop will change to its context automatically at start, so be sure
that any existing Docker container using the default context is **stopped**
before starting Docker Desktop and any of the environments in this folder.

## Development environments

Use the `dev/dev.sh` script to start a development environment.

Example:

```bash
Usage: ./dev.sh {up|down|stop}
```

Once the `wi-dev:x.y.z` container is up, attach a shell to it and run `./gradlew run`
to start the application.

## Containers to generate packages

Use the `ci/ci.sh` script to start provisioned containers to generate packages.

```bash
Usage: ./ci.sh {up|down|stop} [ci]
```

Refer to [scripts/README.md](../scripts/README.md) for details about how to build packages.

[docker-desktop]: https://docs.docker.com/get-docker
[docker-variant]: https://docs.docker.com/desktop/install/linux-install/#differences-between-docker-desktop-for-linux-and-docker-engine
[docker-context]: https://docs.docker.com/desktop/install/linux-install/#context
[wi-repo]: https://github.com/cyb3rhq/cyb3rhq-indexer

## Building Docker images

The [prod](./prod) folder contains the code to build Docker images. A tarball of `cyb3rhq-indexer` needs to be located at the same level that the Dockerfile. Below there is example of the command needed to build the image. Set the build arguments and the image tag accordingly.

```console
docker build --build-arg="VERSION=4.9.1" --build-arg="INDEXER_TAR_NAME=cyb3rhq-indexer-4.9.1-1_linux-x64_cfca84f.tar.gz" --tag=cyb3rhq-indexer:4.9.1 --progress=plain --no-cache .
```

Then, start a container with:

```console
docker run -it --rm cyb3rhq-indexer:4.9.1 
```
