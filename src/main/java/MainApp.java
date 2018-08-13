import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.http.protocol.HTTP.USER_AGENT;

public class MainApp {


    static String[] currencies = {"AUD","GBP","EUR","JPY","CHF","USD","AFN","ALL","DZD","AOA","ARS","AMD","AWG","AUD","AZN","BSD","BHD","BDT"};
    static String start = "TRY";
    static double max = 0;
    static String maxStr = "";

    public static void main(String[] args) {
        for (String curr : currencies){
            double val1 = getRate(start,curr);

            for (String curr2: currencies) {
                if(curr.equals(curr2)) continue;
                    double val2 = getRate(curr,curr2) * val1;
                    double val3 = getRate(curr2,start) * val2;
                    if(val3 > 1.0001){
                        String str = "1 " + start + " : " + val1 + " " + curr + " -> " + val2 + " " + curr2 + " -> " + val3 + " " + start;
                        System.out.println("%" + ((val3-1)*100) + " profit =" + str);
                    }
                    if(val3 > max){
                        max = val3;
                        maxStr = "1 " + start + " : " + val1 + " " + curr + " -> " + val2 + " " + curr2 + " -> " + val3 + " " + start;
                    }


            }
        }

        System.out.println("max :" + maxStr);
    }


    static double getRate(String from, String to) {
        String url = "https://www.xe.com/currencyconverter/convert/?Amount=1&From="+from+"&To="+to;
        String veri =  httpRequst(url);

        Pattern p = Pattern.compile("<span class='uccResultAmount'>(.*?)</span>");
        Matcher m = p.matcher(veri);
        while(m.find()) {
            return Double.parseDouble(m.group(1).replaceAll(",",""));
        }
        return 0;
    }

    static String httpRequst(String url){
        try{

            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(url);

            post.setHeader("User-Agent", USER_AGENT);

            HttpResponse response = client.execute(post);

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            return result.toString();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
