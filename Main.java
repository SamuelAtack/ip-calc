import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("IP: ");
        String input = scanner.nextLine();

        String ipAddress;
        int cidr = -1;

        if (input.contains("/")) {
            String[] parts = input.split("/");
            ipAddress = parts[0];
            cidr = Integer.parseInt(parts[1]);
        } else {
            ipAddress = input;
            System.out.print("Subnet Mask: ");
            String subnetMask = scanner.nextLine();
            String[] octets_string = subnetMask.split("\\.");
            int[] octets = new int[octets_string.length];

            for (int i=0;i<octets_string.length;i++) {
                octets[i] = Integer.parseInt(octets_string[i]);
            }
            cidr = calculateCIDR(octets);
        }
        
        int[] ipOctets = parseIP(ipAddress);
        int[] subnetMask = calculateSubnetMask(cidr);
        int[] networkAddress = new int[4];
        int[] broadcastAddress = new int[4];
        int[] hostMin = new int[4];
        int[] hostMax = new int[4];

        // TODO: calculate network address, broadcast address, and hostmin/max

        System.out.println("Address: " + ipAddress);
        System.out.println("Netmask: " + toIPString(subnetMask) + " = " + cidr);
        System.out.println("Network: " + toIPString(networkAddress) + "/" + cidr);
        System.out.println("Broadcast: " + toIPString(broadcastAddress));
        System.out.println("HostMin: " + toIPString(hostMin));
        System.out.println("HostMax: " + toIPString(hostMax));
        System.out.println("Hosts/Net: " + ((int) Math.pow(2, (32 - cidr)) - 2));
        }

    public static int[] parseIP(String ipAddress) {
        String[] parts = ipAddress.split("\\.");
        int[] octets = new int[4];

        for (int i = 0; i < 4; i++) {
            octets[i] = Integer.parseInt(parts[i]);
        }

        return octets;
    }

    public static int[] calculateSubnetMask(int cidr) {
        int[] mask = new int[4];

        for (int i = 0; i < cidr; i++) {
            mask[i / 8] |= (1 << (7 - (i % 8)));
        }

        return mask;
    }

    public static int calculateCIDR(int[] mask) {
        int cidr = 0;
        for (int octet : mask) {
            while (octet != 0) {
                cidr += octet & 1;
                octet >>= 1;
            }
        }
        return cidr;
    }

    public static String toIPString(int[] octets) {
        return octets[0] + "." + octets[1] + "." + octets[2] + "." + octets[3];
    }
}
