package com.company;

import java.io.*;
import java.net.Socket;

class Client {
    public Client() {
    }

    public static void main(String[] args) throws IOException {
        try { //connect to server
            Socket connection = new Socket("127.0.0.1", 1234); //declare a socket
            System.out.println("Connected: " + connection);
            DataOutputStream output = new DataOutputStream(new BufferedOutputStream(connection.getOutputStream()));
            DataInputStream input = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            InputStream is = connection.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            boolean cont = true;
            System.out.println(input.readUTF());

            while(true) {
                String intro = input.readUTF(); //receive intro from server
                System.out.println(intro);
                int func = Integer.parseInt(br.readLine()); //get the function from user
                output.writeInt(func);
                output.flush();
                switch(func) { //run this case based on user function
                    case 1: //give username and password to server for registering
                        System.out.println(input.readUTF());
                        String username = br.readLine();
                        output.writeUTF(username);
                        output.flush();
                        System.out.println(input.readUTF());
                        String password = br.readLine();
                        output.writeUTF(password);
                        output.flush();
                        System.out.println(input.readUTF());
                        break;
                    case 2:
                        if (input.readUTF().equals("y")) { //get confirmation from server if you have logged in before
                            System.out.println("You are already logged in!");
                        } else {
                            System.out.println(input.readUTF());
                            String given_username = br.readLine();
                            output.writeUTF(given_username);
                            output.flush();
                            System.out.println(input.readUTF());
                            String given_password = br.readLine();
                            output.writeUTF(given_password);
                            output.flush(); //give username and password for the server to check
                            System.out.println(input.readUTF());
                        }
                        break;
                    case 3:
                        if (!input.readUTF().equals("y")) { //get confirmation from server that you are logged in
                            System.out.println("Please log in first!");
                            break;
                        } else {
                            System.out.println(input.readUTF());
                            System.out.println(input.readUTF());
                            System.out.println("Press 1 when you are ready to start the game.");

                            while(true) {
                                int user_ready = Integer.parseInt(br.readLine());
                                if (user_ready == 1) {
                                    break; //stay in loop until player presses 1 to become ready
                                }

                            }

                            System.out.println("Waiting for the other player to be ready.");
                            output.writeUTF("y"); //when player presses 1 server gets a string "y" that lets it know that the player is ready
                            output.flush();
                            System.out.println(input.readUTF());
                            System.out.println(input.readUTF());
                            System.out.println(input.readUTF());
                            System.out.println(input.readUTF());
                            System.out.println(input.readUTF()); //end of countdown
                            if (input.readUTF().equals("y")) { //get string y from server if you are the first player
                                System.out.println(input.readUTF()); //get text
                                String text1 = br.readLine();
                                output.writeUTF(text1); //output written text
                                output.flush();
                                System.out.println("Waiting for the other player to finish.");
                                System.out.println(ois.readObject());
                                System.out.println(input.readUTF()); //get result from server
                                cont = false; //cont variable lets the program know that the user has played and it should quit after finishing
                                break;


                            }
                            if (input.readUTF().equals("n")) { //get string n if you are the second player
                                System.out.println("Other player is typing, be ready to start.");
                                System.out.println(input.readUTF()); //get text
                                String text2 = br.readLine();
                                output.writeUTF(text2); //submit written text
                                output.flush();
                                System.out.println(ois.readObject());
                                cont = false; //cont var to let the program know the game is finished and it should quit.
                            }
                            System.out.println(input.readUTF());break; //get confirmation if the result was a record
                        }
                    default:
                        throw new IllegalStateException("Invalid function: " + func); //throw exception if user enters invalid function
                }
                if (cont==false){
                    System.out.println("Thanks for playing!\nQuitting game.");
                    break; //break out of loop if the player has finished
                }

            }

            connection.close(); //close sockets and streams
            input.close();
            output.close();
        } catch (IOException | ClassNotFoundException var16) {
            System.out.println(var16.getMessage());
        }
    }
}
