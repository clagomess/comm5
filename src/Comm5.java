import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class Comm5 {
    public static Socket socket;
    public static PrintWriter pw;
    public static HashMap<Integer, Boolean> sensor;

    public boolean abreConexao(String ip){
        try {
            socket = new Socket(ip, 5000);
            socket.setKeepAlive(true);

            if(socket.isConnected()) {
                this.sensor();
                pw = new PrintWriter(socket.getOutputStream(), true);
                pw.write("out\r\n");
                pw.flush();
            }

            return true;
        } catch (IOException e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void rele(int posicao, boolean atracar){
        if(socket.isConnected()) {
            String arg = String.format("%s %d\r\n", (atracar ? "set" : "reset"), posicao);
            pw.write(arg);
            pw.flush();
        }
    }

    private void sensor(){
        Thread comm5Run = new Thread(new Runnable() {
            @Override
            public void run() {
                if(!socket.isConnected()) {
                    return;
                }

                BufferedReader br = null;
                String line;

                try {
                    br = new BufferedReader(new InputStreamReader(Comm5.socket.getInputStream()));
                } catch (IOException e){
                    System.out.println(e.getMessage());
                }

                while (true){
                    if(!Comm5.socket.isConnected() || br == null){
                        break;
                    }

                    try {
                        if(br.ready()) {
                            line = br.readLine();

                            if(line.substring(0, 3).equals("210") && !line.trim().substring(4).equals("OK")){
                                Long lValue = Long.parseLong(line.trim().substring(4), 16);
                                String sValue = Long.toString(lValue, 2);

                                sensor = new HashMap<>();
                                int sensorNum = 1;

                                for(int i = sValue.length() - 1; i >= 0; i--) {
                                    sensor.put(sensorNum, (sValue.charAt(i) == '1'));
                                    sensorNum++;
                                }
                            }
                        }else{
                            Comm5.pw.write("query\r\n");
                            Comm5.pw.flush();
                        }
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.getMessage();
                    }
                }
            }
        });

        comm5Run.start();
    }

    public void fechaConexao(){
        try {
            socket.close();
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
