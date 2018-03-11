package server;

import client.ClientHandler;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private final static int PORT = 8189;
    private CopyOnWriteArrayList<ClientHandler> clients;
    private AuthService authService;

    public Server()
    {
        ServerSocket serverSocket = null;
        Socket socket = null;
        clients = new CopyOnWriteArrayList<>();

        try
        {
            serverSocket = new ServerSocket(PORT);
            authService = new BaseAuthService();
            authService.start(); //пока что пустой! Заглушка
            System.out.println("Сервер запущен, ждем клиентов");
            while (true)
            {
                socket = serverSocket.accept();
                clients.add(new ClientHandler(socket, this));
                System.out.println("Клиент подключился");
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
                serverSocket.close();
                socket.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            authService.stop(); //заглушка
        }
    }

    public boolean isNickBusy(String nick)
    {
        System.out.println(nick);
        for(ClientHandler c: clients)
        {
            if(c.getName().equals(nick))
            {
                return true;
            }
        }
        return false;
    }

    public AuthService getAuthService()
    {
        return authService;
    }
}

