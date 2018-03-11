package client;

import server.Server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler
{
    private Server server;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String name;

    public ClientHandler(Socket socket, Server server)
    {
        try
        {
            this.server = server;
            this.socket = socket;
            name = "undefined";
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        new Thread(() ->
        {
            try
            {
                //Авторизация
                while(true)
                {
                    String msg = in.readUTF();
                    if(msg.startsWith("/auth"))
                    {
                        String[] elements  = msg.split(" ");
                        String nick = server.getAuthService().getNickByLoginPass(elements[1], elements[2]);
                        System.out.println(nick);
                        if (nick != null)
                        { // если пользователь указал правильные логин/пароль
                            if(!server.isNickBusy(nick))
                            {
                                sendMessage("/authok " + nick);
                                sendMessage("You are logged in. Welcome!");
                                this.name = nick;
                                break;
                            }
                            else
                            {
                                sendMessage("Account is already used.");
                            }
                        }
                        else
                        {
                            sendMessage("Not valid username or password");
                        }
                    }
                    else
                    {
                        sendMessage("First, you need to log in!");
                    }
                } //пока не прервется цикл авторизации, не начнется цикл приема сообщений
                while(true)
                {
                    String msg = in.readUTF();
                    System.out.println("client: " + msg);
                    if (msg.startsWith("/"))
                    {
                        if(msg.equalsIgnoreCase("/end"))
                        {
                            break;
                        }
                        else
                        {
                            sendMessage("There is no scuh command!");
                        }
                    }
                    // sendMessage("echo: " + msg);
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    socket.close();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public String getName()
    {
        return name;
    }

    public void sendMessage(String msg)
    {
        try
        {
            out.writeUTF(msg);
            out.flush();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}