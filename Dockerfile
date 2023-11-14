# Use an official Maven image with Java 11 as a parent image
FROM maven:3.6.3-jdk-11

# Set the working directory in the container
WORKDIR /app

# Copy the project files into the container at /app
COPY . /app

# Build the project
RUN mvn clean package

# Run the application
CMD ["java", "-cp", "target/tsp-solver-0.1-SETUP.jar", "com.elmika.tsp.TravellingSalesman"]
