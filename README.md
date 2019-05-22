# 8INF843 - Systèmes répartis - TP1

Il s'agit du readme du TP1 réalisé dans le cadre du cours de systèmes répartis de l'UQAC.

> Orann WEBER

## Contexte

L’objectif de ce travail est l’implémentation d’une plateforme de calcul collaborative et distribuée. L’idée est de permettre à un client, avec ressources limitées (CPU, mémoire, etc.), l’exécution distribuée de certaines tâches quotidiennes. Pour ce faire, le client délègue l’accomplissement d’une ou plusieurs tâches à un serveur (ou machine) distant(e) en envoyant une commande spécifique.

Le serveur extrait les informations nécessaires (nom de la classe, méthode, paramètres), exécute la méthode demandée et retourne le résultat au client, qui montre l'information dans son interface. Notons que vous avez besoin de compiler la classe, charger la classe, exécuter la méthode (service) demandée dans cette classe, et retourner le résultat.

## Prérequis

- Java SDK >= 8

## Installation & Exécution

- Cloner le projet
- Importez-le dans votre IDE préféré
- Exécuter la classe main du fichier ApplicationServeur.java situé dans le paquet "serveur"
- Exécuter la classe main du fichier ApplicationClient.java situé dans le paquet "client".

## Utilisation

- Dans le main du fichier ApplicationClient.java situé dans le paquet "client", nous pouvons renseigner la commande à exécuter par le serveur.
- Une fois les deux main (du serveur et du client) lancés, tout est automatique et tout s'affiche dans la console.
- Si vous le souhaitez, vous pouvez récupérer le résultat de la commande dans le fichier output/resultats.txt
