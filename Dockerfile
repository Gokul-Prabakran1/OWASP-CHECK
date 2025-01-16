# Use an outdated base image
FROM ubuntu:16.04

# Run as root user (default; no USER directive used)
USER root

# Install outdated and vulnerable packages
RUN apt-get update && apt-get install -y \
    wget \
    curl \
    openssh-server \
    python2.7 \
    software-properties-common \
    && apt-get clean

# Install a vulnerable version of OpenSSL
RUN apt-get update && apt-get install -y \
    openssl=1.0.2g-1ubuntu4.20 \
    && apt-get clean

# Expose SSH port
EXPOSE 22

# Add a hardcoded root password (insecure practice)
RUN echo 'root:rootpassword123' | chpasswd

# Copy vulnerable application code
COPY app /app
WORKDIR /app

# Run application with insecure permissions
RUN chmod 777 /app

# Start an outdated Python HTTP server (vulnerable to directory traversal)
CMD ["python2.7", "-m", "SimpleHTTPServer", "8080"]
