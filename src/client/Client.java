package client;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {

        try{
            Socket cS = new Socket("127.0.0.1", 1337);

            ObjectOutputStream objOutStream = new ObjectOutputStream(cS.getOutputStream());

    //        ObjectInputStream objInStream = new ObjectInputStream(cS.getInputStream());

            OutputStreamWriter os = new OutputStreamWriter(objOutStream);

            //ObjectInputStream objInStream = new ObjectInputStream();

            BufferedWriter bw = new BufferedWriter(os);

            Scanner sc = new Scanner(System.in);

            String intro = "*** Bienvenue au portail d'inscription de cours de l<UdeM ***\n";

            String choixSession = "Veuillez choisir la session pour laquelle vous voulez consulter la liste de cours:\n\n1. Automne\n2.Hiver\n3.Ete\n";

            System.out.println(intro + choixSession);

            while(sc.hasNext()){

                String choixSessionUser = sc.nextLine();
                if (choixSessionUser.equals("1")){
                    System.out.println("Les cours offerts pour la session d'automne sont:");
           //         objOutStream.write("CHARGER," + choixSessionUser);
            //        objOutStream.flush();
                    Object Test;
                    Test = new String("CHARGER," + choixSessionUser );
                    objOutStream.writeObject(Test);
                    objOutStream.flush();
               //     bw.write(Test );
                //    bw.flush();
    //                bw.close();
                }
                else if (choixSessionUser.equals("2")){
                    System.out.println("Les cours offerts pour la session d'hiver sont:");
                }
                else if (choixSessionUser.equals("3")){
                    System.out.println("Les cours offerts pour la session d'ete sont:");
                }
                else{
                    System.out.println("Choix de session invalide, les choix sont 1 pour automn, 2 pour hiver et 3 pour ete.");
                    break;
                }


            }

            bw.close();
            sc.close();
            cS.close();

        }
        catch (ConnectException x){
            System.out.println("Connexion ipossible aui port 1337: pas de serveur.");
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }

}

