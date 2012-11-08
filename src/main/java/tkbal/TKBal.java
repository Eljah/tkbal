package tkbal;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import java.io.*;
//import java.io.ByteBuffer;

public class TKBal extends MIDlet implements CommandListener {

    private Command cmd_exit;
    private Command submit = new Command("Submit", Command.SCREEN, 1);

    private Display display;

    private Form form;
    TextField number = new TextField("Номер билета", null, 10, TextField.DECIMAL);
    TextField captcha = new TextField("Введите код", null, 4, TextField.DECIMAL);
    String viewstate;
    String eventvalidation;
    //http://81.23.146.8/default.aspx

    private RecordStore rs = null;

    public void accessHttp() {
        HttpConnection connection = null;
        InputStream inputstream = null;
        String str = "";
        try {
            connection = (HttpConnection) Connector.open("http://81.23.146.8/default.aspx");
            //HTTP Request
            //connection.setRequestMethod(HttpConnection.GET);
            //connection.setRequestProperty("Content-Type", "//text plain");
            //connection.setRequestProperty("Connection", "close");

            // HTTP Response
            System.out.println("Status Line Code: " + connection.getResponseCode());
            System.out.println("Status Line Message: " + connection.getResponseMessage());
            //if ((connection.getResponseCode() == HttpConnection.HTTP_OK)||(connection.getResponseCode() == 302)) {
            System.out.println(
                    connection.getHeaderField(0) + " " + connection.getHeaderFieldKey(0));
            System.out.println(
                    "Header Field Date: " + connection.getHeaderField("date"));
            System.out.println(
                    "Header Field Date: " + connection.getHeaderField("Content-Type"));
            inputstream = connection.openInputStream();
            int length = (int) connection.getLength();
            System.out.println("Responce size (chars)" + connection.getLength());
            if (length != -1) {
                byte incomingData[] = new byte[length];
                //inputstream.read(incomingData);
                //str = new String(incomingData);
                Reader r = new InputStreamReader(inputstream, "UTF-8");
                StringBuffer sb = new StringBuffer();
                int ch;
                while ((ch = r.read()) != -1)

                {
                    if ((char) ch != '\n')
                        sb.append((char) ch);

                }
                str = sb.toString();

            } else {

                System.out.println("Something went wrong");
                byte incomingData[] = new byte[length];
                //inputstream.read(incomingData);
                //str = new String(incomingData);
                Reader r = new InputStreamReader(inputstream, "windows-1251");
                StringBuffer sb = new StringBuffer();
                int ch;
                boolean tagflag = false;
                while ((ch = r.read()) != -1) {
                    /*    if ((char) ch == '<') {
                      tagflag = true;
                  }
                  */
                    if ((char) ch != '\n')
                        sb.append((char) ch);
                    /*}

                    if ((char) ch == '>') {
                        tagflag = false;
                    } */
                }
                str = sb.toString();

            }
            System.out.println(str);

            int indState = str.indexOf("id=\"__VIEWSTATE\" value=\"") + 24;
            viewstate = str.substring(indState, indState + 116);
            System.out.println(viewstate);

            int indState2 = str.indexOf("id=\"__EVENTVALIDATION\" value=\"") + 30;
            eventvalidation = str.substring(indState2, indState2 + 64);
            System.out.println(eventvalidation);

            int index = str.indexOf("CaptchaImage.axd");
            //System.out.println(index);
            //System.out.println(index+58);
            str = str.substring(index, index + 58);
            //form.append("Value: " + str);

            //form.append("Value: " + str2);
            System.out.println(str);
            //System.out.println(str2);
            //}

            //geting captcha image
            connection.close();
            HttpConnection connection2 = (HttpConnection) Connector.open("http://81.23.146.8/" + str);
            //HttpConnection connection2 = (HttpConnection) Connector.open("http://www.transkart.ru/111/img/btn_1.png");
            //http://www.transkart.ru/111/img/btn_1.png
            System.out.println("Status Line Code: " + connection2.getResponseCode());
            System.out.println("Status Line Message: " + connection2.getResponseMessage());
            //if ((connection.getResponseCode() == HttpConnection.HTTP_OK)||(connection.getResponseCode() == 302)) {
            System.out.println(
                    connection2.getHeaderField(0) + " " + connection2.getHeaderFieldKey(0));
            System.out.println(
                    "Header Field Date: " + connection2.getHeaderField("date"));
            System.out.println(
                    "Header Field Date: " + connection2.getHeaderField("Content-Type"));
            //inputstream = connection2.openInputStream();
            length = (int) connection2.getLength();
            System.out.println("Responce size (chars)" + length);
            DataInputStream dis = connection2.openDataInputStream();
            byte[] bytes = new byte[length];
            dis.readFully(bytes);
            Image img = Image.createImage(bytes, 0, length);
            //Graphics g=new Graphics();
            //g.drawImage(img,1,1,1);
            form.append(img);
            System.out.println("appended");


        } catch (IOException error) {
            System.out.println("Caught IOException: " + error.toString());
            form.insert(2, new StringItem("", "К сожалению, сайт недоступен!"));
            ;
        } finally {
            if (inputstream != null) {
                try {
                    inputstream.close();
                } catch (Exception error) {
                    /*log error*/
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception error) {
                    /*log error*/
                }
            }
        }

    }


    public void startApp() throws MIDletStateChangeException {
        try {
            rs = RecordStore.openRecordStore("tknumber", true);
            if (rs.getNumRecords() > 0) {
                InputStream is = new ByteArrayInputStream(rs.getRecord(1));

                int ch = -1;
                StringBuffer sb1 = new StringBuffer();
                Reader r2 = new InputStreamReader(is, "UTF-8");

                while ((ch = r2.read()) != -1) {

                    sb1.append((char) ch);

                }
                String outrs = sb1.toString();
                System.out.println(outrs);
                number.setString(outrs);
            }
            rs.closeRecordStore();
        } catch (RecordStoreException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        display = Display.getDisplay(this);
        form = new Form("Simple");
        form.setCommandListener(this);

        System.out.println("Starting app");

        cmd_exit = new Command("Exit", Command.EXIT, 0);
        form.addCommand(cmd_exit);
        form.addCommand(submit);

        form.append(number);
        //form.append(captcha);
        //form.append("Hello j2me");

        Runtime rt = Runtime.getRuntime();
        //form.append("Free Memory: " + rt.freeMemory());
        //form.append("Total Memory: " + rt.totalMemory());


        accessHttp();
        form.append(captcha);
        display.setCurrent(form);
    }

    public void pauseApp() {

    }

    public void destroyApp(boolean unconditional) throws MIDletStateChangeException {
        notifyDestroyed();
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmd_exit) {
            try {
                destroyApp(true);
            } catch (MIDletStateChangeException e) {
                showException(e);
            }
        }
        if (c == submit) {
            form.removeCommand(submit);
            String card = requestDataHttp();
            card = card.substring(card.indexOf("Карта"));
            ///Карта
            System.out.println(card);
            String money=card.substring(card.indexOf("с:")+2,card.indexOf("тар."));
            System.out.println(money);
            String lastTransport=card.substring(card.indexOf("е:")+2,card.indexOf("Операция"));
            System.out.println(lastTransport);
            String lastIncome=card.substring(card.indexOf("я:")+2);
            System.out.println(lastIncome);
            String dateEnd=card.substring(card.indexOf("по ")+3,card.indexOf("Ресурс"));
            System.out.println(dateEnd);
            StringItem responce = new StringItem("Состояние счета", money);
            StringItem responce2 = new StringItem("Последняя операция списания", lastTransport);
            StringItem responce3 = new StringItem("Последняя операция зачисления", lastIncome);
            StringItem responce4 = new StringItem("Карта действует до", dateEnd);
//Ñâîéñòâà ïðîåçäíîãî áèëåòàÂâåäèòå ïàðàìåòðû Íîìåð êàðòû    &nbsp;Êîä ïðîâåðêè       Êàðòà ¹ 3895232407Ïðîåçäíîé áèëåò:Ýëåêòðîííûé êîøåëåêÄåéñòâóåòc 01.11.2012 ïî 01.11.2014Ðåñóðñ ñåé÷àñ:496 òàð.åä. Ïîñëåäíåå ïðåäúÿâëåíèå â òðàíñïîðòå:06.11.2012 10:26:00Ìàðøðóò:Äóáëèðóþùèé Ìàðøðóò ¹21Âèä òðàíñïîðòàÒðîëëåéáóñûÎïåðàöèÿÏðîõîä ñî ñïèñàíèåì ðåñóðñàÄàòà è âðåìÿ ïîïîëíåíèÿ:01.11.2012 10:29:00Ïóíêò ïîïîëíåíèÿÃëàâïî÷òàìò óë. Êðåìëåâñêàÿ ä.8, ÀÐÌ/êèîñê 20911Ðåñóðñ ïîïîëíåí íà:500 òàð.åä.
            //responce.setString();

            form.delete(0);
            form.delete(0);
            form.delete(0);


            form.append(responce);
            form.append(responce2);
            form.append(responce3);
            form.append(responce4);
        }
    }

    public String requestDataHttp() {
        String str = null;
        try {
            try {
                rs = RecordStore.openRecordStore("tknumber", true);
                System.out.println(rs.getNumRecords());
                if (rs.getNumRecords() == 0) {
                    rs.addRecord(number.getString().getBytes("UTF-8"), 0, number.getString().getBytes("UTF-8").length);
                } else {
                    rs.setRecord(1, number.getString().getBytes("UTF-8"), 0, number.getString().getBytes("UTF-8").length);
                }
                rs.closeRecordStore();
            } catch (RecordStoreException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            HttpConnection connection3 = (HttpConnection) Connector.open("http://81.23.146.8/default.aspx",Connector.READ_WRITE);
            connection3.setRequestMethod(HttpConnection.POST);
            connection3.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            String strToPost = "cardnum" + "=" + number.getString() + "&" + "checkcode" + "=" + captcha.getString() + "&" + "__EVENTVALIDATION" + "=" + URLEncoder.encode(eventvalidation) + "&" + "__VIEWSTATE" + "=" + URLEncoder.encode(viewstate);
            OutputStream os = connection3.openDataOutputStream();
            System.out.println(strToPost);
            os.write(strToPost.getBytes());
            System.out.println("Status Line Code: " + connection3.getResponseCode());
            System.out.println("Status Line Message: " + connection3.getResponseMessage());
            //if ((connection.getResponseCode() == HttpConnection.HTTP_OK)||(connection.getResponseCode() == 302)) {
            System.out.println(
                    connection3.getHeaderField(0) + " " + connection3.getHeaderFieldKey(0));
            System.out.println(
                    "Header Field Date: " + connection3.getHeaderField("date"));
            System.out.println(
                    "Header Field Date: " + connection3.getHeaderField("Content-Type"));
            InputStream inputstream = connection3.openInputStream();
            int length = (int) connection3.getLength();
            System.out.println("Responce size (chars)" + connection3.getLength());
            if (length != -1) {
                byte incomingData[] = new byte[length];
                //inputstream.read(incomingData);
                //str = new String(incomingData);
                Reader r = new InputStreamReader(inputstream, "UTF-8");
                StringBuffer sb = new StringBuffer();
                boolean tagflag = false;
                int ch;

                while ((ch = r.read()) != -1) {
                    if ((char) ch == '<') {
                        tagflag = true;
                    }

                    if ((!tagflag) && ((char) ch != '\n')) {
                        sb.append((char) ch);
                    }

                    if ((!tagflag) && ((char) ch == '\n')) {
                        sb.append(' ');
                    }

                    if ((char) ch == '>') {
                        tagflag = false;
                    }
                }
                str = sb.toString();

            } else {

                System.out.println("Something went wrong");
                byte incomingData[] = new byte[length];
                //inputstream.read(incomingData);
                //str = new String(incomingData);
                Reader r = new InputStreamReader(inputstream, "windows-1251");
                StringBuffer sb = new StringBuffer();
                int ch;
                boolean tagflag = false;
                while ((ch = r.read()) != -1) {
                    /*    if ((char) ch == '<') {
                      tagflag = true;
                  }
                  */
                    if ((char) ch != '\n')
                        sb.append((char) ch);
                    /*}

                    if ((char) ch == '>') {
                        tagflag = false;
                    } */
                }
                str = sb.toString();

            }
            System.out.println(str);

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        return str;
    }

    public void showException(Exception e) {
        Alert alert = new Alert("Error");
        alert.setString(e.getMessage());
        alert.setType(AlertType.ERROR);
        alert.setTimeout(Alert.FOREVER);
        display.setCurrent(alert);
    }
}
