

public class Main {
    public static void main(String[] args)  {
        Weather weather = new Weather();
        weather.getData();
        System.out.println(weather.getPressure());
        System.out.println(weather.getMinTempDiffDay());
    }

}
