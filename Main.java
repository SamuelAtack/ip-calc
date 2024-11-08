import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String address;
        String netmask_slash;
        String netmask_octets;
        String network;
        String broadcast;
        String hostMin;
        String hostMax;
        int net;

        Scanner in = new Scanner(System.in);
        System.out.println("IP Address: ");
        String input = in.nextLine();

        if (input.contains("/")) {
            address = input.substring(0,input.indexOf("/"));
            netmask_slash = input.substring(input.indexOf("/") + 1);
            netmask_octets = "";
            for (int i = 0; i<Integer.parseInt(netmask_slash)/8; i++) {
                netmask_octets = netmask_octets + "255."; 
            }
            netmask_octets = netmask_octets + "0";
            network = address.substring(0,address.indexOf(".", address.indexOf(".", address.indexOf(".") + 1) + 1)) + ".0/" + netmask_slash;
        } else {
            address = input;
            System.out.println("Subnet mask:");
            netmask_octets = in.nextLine();
            netmask_slash = String.valueOf((netmask_octets.length() - netmask_octets.replace("255", "").length())/3 * 8);
            network = address.substring(0,address.indexOf(".", address.indexOf(".", address.indexOf(".") + 1) + 1)) + ".0/" + netmask_slash;
        }

        String out = "Address: " + address + "\n" + "Netmask: " + netmask_octets + " = " + netmask_slash + "\n" + "Network: " + network;

        System.out.println(out);
    }
}
