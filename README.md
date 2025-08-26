# Shop App — Local Deployment

This project demonstrates building Spring Boot microservices and a React frontend, publishing the images to Docker Hub, deploying them to Kubernetes (Kind) with Ingress NGINX, and configuring a self-signed TLS certificate for the domain `shop-app.com`.

## Table of Contents
- [Requirements](#requirements)
- [1. Build and Push Docker Image](#1-build-and-push-docker-image)
- [2. TLS Certificate](#2-tls-certificate)
- [3. Kind Cluster + Ingress NGINX](#3-kind-cluster--ingress-nginx)
- [4. Apply Kubernetes Manifests](#4-apply-kubernetes-manifests)
- [5. Verification](#5-verification)
---

## Requirements
- [Docker](https://docs.docker.com/desktop/)
- Kubernetes CLI `kubectl`
- [kind](https://kind.sigs.k8s.io/)
- [helm](https://helm.sh/)
- [OpenSSL](https://slproweb.com/products/Win32OpenSSL.html)
- Java + Maven
- Docker Hub account + `docker login`

---

## 1. Build and Push Docker Image

### Backend (each microservice)
Before building with Jib, add your Docker Hub username and password in `.mvn/maven.config`:
```bash
mvn compile jib:build
```

### Frontend
You can use a different image name and tag if needed:
```bash
docker build -t frontend .
docker tag frontend:latest studentpp1/frontend:latest
docker push studentpp1/frontend:latest
```

---

## 2. TLS Certificate
+ Create a self-signed certificate for shop-app.com and configure a Kubernetes TLS secret.
```bash
openssl req -x509 -nodes -days 365 -newkey rsa:2048 `
  -keyout shop-app.com.key -out shop-app.com.crt `
  -subj "/CN=shop-app.com"
```
```bash
kubectl delete secret shop-tls --ignore-not-found
kubectl create secret tls shop-tls --cert=shop-app.com.crt --key=shop-app.com.key
```

---

## 3. Kind Cluster + Ingress NGINX
+ Create the Kind cluster and install Ingress NGINX with NodePort (HTTP 30080, HTTPS 30443).
```bash
cd k8s/kind
kind create cluster --config kind-config.yaml
```
```bash
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update
```
```bash
helm upgrade --install nginx-ing ingress-nginx/ingress-nginx -n ingress-nginx --create-namespace --set controller.service.type=NodePort --set controller.service.nodePorts.http=30080 --set controller.service.nodePorts.https=30443 --atomic --wait
```
+ Then go back to manifests:
```bash
cd ..
cd manifests
```

---

## 4. Apply Kubernetes Manifests
```bash
kubectl apply -f infrastructure
kubectl apply -f applications
```

---

## 5. Verification
+ Check that Ingress NGINX is running and resources were created:
```bash
kubectl get pods -n ingress-nginx
kubectl get ingress
```
+ Check if all pods are running
```bash
curl.exe -kI https://shop-app.com/ -H "Host: shop-app.com" -k
```
---

To resolve the domain locally, add to your hosts file:
+ Linux/macOS: /etc/hosts
+ Windows: C:\Windows\System32\drivers\etc\hosts 

Add this line: `127.0.0.1 shop-app.com`

---

Test with curl:
```bash
 curl.exe -kI https://shop-app.com/ -H "Host: shop-app.com" -k  
```