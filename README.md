# SCARF-GraphRepository
SCARF-GraphRepository is the graph based database together with the management application *Pertos* used to store, retrieve, and search for reusable cloud application topologies. 

# Build Source & Run
1. Build *Pertos* application: `cd topologyDis-parent && mvn install && cd ..`
2. Copy the WAR application to the Deployment directory: `cp ./topologyDis-parent/topologyDis-webresource/target/pertos.war ./Deployment/pertos.war`
3. Build the docker container: `docker-compose build`
4. Run: `docker-compose up`

# Running the SCARF toolchain
If you want to run the complete tool chain, please go to [SCARF](https://github.com/sgomezsaez/SCARF)