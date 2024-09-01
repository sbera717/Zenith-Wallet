**Have you ever forgotten to save money for gifts for your loved ones, or do you find physical wallets cumbersome? I'm excited to introduce you to Zenith Wallet—a next-generation e-wallet designed to do more than just store your money.**

# **Zenith-Wallet**
An advanced E-wallet that goes beyond the basics.

Zenith Wallet is more than a traditional e-wallet; it's your personal savings companion. With features that allow you to save money for yourself and your loved ones, you'll never miss an opportunity to buy something special. What's more, the money you save with us earns interest, encouraging you to save even more. This way, you can finally afford that precious item you've always wanted.

# **How Zenith Wallet Works**
**Set a Savings Goal:** Choose a goal, like buying a laptop, with a specific monthly amount and duration (e.g., 10000 for 10 months).

**Earn Interest:** As you save, Zenith applies interest to your accumulated funds, rewarding you for staying on track.

**Automated Monthly Deductions:** Zenith  deducts the specified amount from your wallet balance each month, ensuring you consistently save toward your goal.

**User-to-User Transfers:** Easily transfer money between users, making it simple to send or receive funds.

 #                                                                         **Technical Details**
**Keycloak for Security:** Used for strong security and Single Sign-On (SSO) capabilities.

**Inter-Service Communication with Kafka:** Leveraged for seamless communication between microservices with a Dead Letter Queue (DLQ) to handle message failures.

**Scalability with Kubernetes:** Deployed on Kubernetes to ensure high scalability and availability.

**CI/CD Pipeline:** Set up using Jenkins for continuous integration and Argo CD for continuous deployment, with SonarQube for code quality checks.

**Reverse Proxy and Load Balancer:** A reverse proxy enhances security, and a load balancer efficiently distributes traffic across services.

**Helm Charts for Deployment:** Used for managing Kubernetes deployments, providing a standardized and efficient deployment process.

#                                                        **To Run This Application on Your System:**

**Clone the Repository:** Clone the full repository, where each of the 5 microservices is organized in separate folders.

**Run the Applications:** Start all the microservices. Ensure that Keycloak and Kafka are installed and running on your system.

**Set Up Keycloak and Kafka:** You can either install Keycloak and Kafka locally or use Docker for a simpler setup. Using Docker is recommended as it is easier and more efficient.

For Keycloak, use the below command. Once Keycloak is running, you need to create a realm and configure it by giving the necessary permissions to the admin-cli and your client. After these configurations are done, my Spring Boot application will automatically create accounts in Keycloak and handle the necessary tasks

**docker run -p 8080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin -v keycloak_data:/opt/keycloak/data quay.io/keycloak/keycloak:25.0.1 start-dev**

For Kafka, I will provide a Docker Compose file. Simply run the Docker Compose file, and both Kafka and Zookeeper will be up and running.

**Install an IDE:** It is recommended to install an Integrated Development Environment (IDE) like IntelliJ IDEA or Eclipse. This will make it easier to test the application.

**Configure SMTP Server:** The application includes an SMTP server for sending emails in Notification Service. You can configure it to use your own SMTP server or any external service like Google's SMTP server.

![Wallet-Final](https://github.com/user-attachments/assets/af7bcb38-8f91-489d-b0e1-7e154bcbad17)

