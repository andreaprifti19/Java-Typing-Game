# Java Multiplayer Typing Game

This project is a **client-server based multiplayer typing game** written in **Java**.  
Players can register, log in, team up, and compete by typing a given text accurately and quickly.  
The server manages multiple clients simultaneously using multithreading and maintains a leaderboard of the best teams.

---

## Features

- **Register and Log In** system
- **Team Formation** (2 players per team)
- **Real-time Typing Race** based on speed and accuracy
- **Leaderboard** tracking the best team times
- **Multithreaded Server** supporting multiple players
- **Client-Server Communication** over TCP sockets

---

## Technologies Used

- **Java SE**
- **TCP Sockets** (ServerSocket, Socket)
- **Multithreading** (Thread, Runnable)
- **Object Streams** for sending objects across network

---

## How to Run

### Server

1. Compile the server code:
    ```bash
    javac ThreadHandler.java ClientHandler.java Db.java Player.java Team.java
    ```
2. Run the server:
    ```bash
    java Thread_Handler
    ```
   
> The server will start and listen for client connections on `port 1234`.

---

### Client

1. Compile the client code:
    ```bash
    javac Main.java
    ```
2. Run the client:
    ```bash
    java Client
    ```
   
> The client will connect to `localhost` on `port 1234`.  
> Make sure the server is running before starting the client.

---

## Project Structure

| File | Purpose |
|:-----|:--------|
| `ThreadHandler.java` | Main server entry point, manages incoming clients |
| `ClientHandler.java` | Handles each client's actions on the server |
| `Db.java` | Manages user registration, login, and leaderboard data |
| `Player.java` | Represents a player |
| `Team.java` | Manages team creation and readiness |
| `Main.java` | Client-side logic for connecting and playing |

---

## Notes

- This project uses **local IP (127.0.0.1)** — for remote play, replace it with your server’s public IP.
- If you want multiple clients, **open several terminals** and run the client in each.
- Passwords are stored in memory only (no database integration).

---

## License

This project is for educational purposes.  
Feel free to modify and expand it!

