# Soccer car game

This is a simple multiplayer soccer car game application. Each player have a car that can move in 4 directions (up, down, left, right). The game ends after 2 minutes and the player with the most goals wins.

<div align="center">
    <img src="https://github.com/bastian-albaut/Car-game-app/blob/main/docs/images/headerReadme.png" width="60%" />
</div>

## Built With

- [Quarkus](https://quarkus.io/) A Java framework for building cloud-native applications.
- [Java](https://www.java.com/) A high-level, class-based, object-oriented programming language.
- [Websockets](https://developer.mozilla.org/en-US/docs/Web/API/WebSockets_API) A communication protocol that makes it possible to establish a two-way communication channel between a server and a client.
- [React](https://reactjs.org/) A JavaScript library for building user interfaces.
- [Typescript](https://www.typescriptlang.org/) A superset of JavaScript that adds optional types to the language.
- [Material-UI](https://material-ui.com/) A popular React UI framework.
- [Sass](https://sass-lang.com/) A CSS extension language.
- [Docker](https://www.docker.com/) A platform for building, sharing, and running applications with containers.

## Getting Started

### Prerequisites

- You need to have [Docker](https://www.docker.com/get-started/) installed on your machine.


### Installation

1. Pull the docker image from the docker hub:
    ```sh
    docker pull bastian8dev/socket_car_game-jvm:latest
    ```

2. Run the docker image:
    ```sh
    docker run -i --rm -p 8080:8080 bastian8dev/socket_car_game-jvm:latest
    ```

3. Open two browsers and go to `http://localhost:8080/`

4. Enjoy the game!

## Usage

- Use the arrow keys to move the car.
- The game ends after 2 minutes.
- The player with the most goals wins.

## Demo

The project is available on the following link:
[Soccer Car Game](https://socket-car-game-jvm-vkrm.onrender.com/)

*You have to open the link in two different browsers to play the game.*
