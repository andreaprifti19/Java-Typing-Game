package com.company;

import java.net.Socket;

public class Player {
    public Socket s;
    public String ID;
    public boolean ready = false;
    public boolean finished = false;
    public boolean correct = false;

    public Player(Socket s, String user) {
        this.s = s;
        this.ID = user;
    }
}
