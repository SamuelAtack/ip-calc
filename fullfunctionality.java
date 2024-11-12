import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        String ipAddress;
        int cidr=-1;

        while(true){
            System.out.print("IP (e.g., 192.168.1.1/24): ");
            String input=scanner.nextLine().trim();

            if(input.contains("/")){
                String[] parts=input.split("/");
                ipAddress=parts[0].trim();
                try{
                    validateIPAddress(ipAddress);
                    cidr=Integer.parseInt(parts[1].trim());
                    if(cidr<0||cidr>32){
                        throw new NumberFormatException("CIDR must be between 0 and 32");
                    }
                    break;
                }catch(Exception e){
                    System.out.println("Invalid input. I am really glad my code managed to find this incorrect input, unless its a false negative :(. Please enter a valid IP in format x.x.x.x/xx.");
                }
            }else{
                ipAddress=input;
                try{
                    validateIPAddress(ipAddress);
                    while(true){
                        System.out.print("Enter CIDR notation (e.g., 24): ");
                        try{
                            cidr=Integer.parseInt(scanner.nextLine().trim());
                            if(cidr<0||cidr>32){
                                throw new NumberFormatException("CIDR must be between 0 and 32");
                            }
                            break;
                        }catch(NumberFormatException e){
                            System.out.println("Invalid CIDR value. Please enter a number between 0 and 32.");
                        }
                    }
                    break;
                }catch(Exception e){
                    System.out.println("Invalid IP address. Please enter a valid IP.");
                }
            }
        }

        int[] ipOctets=parseIP(ipAddress);
        int[] subnetMask=calculateSubnetMask(cidr);
        int[] networkAddress=new int[4];
        int[] broadcastAddress=new int[4];
        int[] hostMin=new int[4];
        int[] hostMax=new int[4];

        for(int i=0;i<4;i++){
            networkAddress[i]=ipOctets[i]&subnetMask[i];
        }

        for(int i=0;i<4;i++){
            broadcastAddress[i]=networkAddress[i]|~subnetMask[i]&0xFF;
        }

        System.arraycopy(networkAddress,0,hostMin,0,4);
        hostMin[3]+=1;

        System.arraycopy(broadcastAddress,0,hostMax,0,4);
        hostMax[3]-=1;

        int hostsPerNet=calculateHostsPerNet(cidr);

        System.out.println("Address: "+ipAddress);
        System.out.println("Netmask: "+toIPString(subnetMask)+" = "+cidr);
        System.out.println("Network: "+toIPString(networkAddress)+"/"+cidr);
        System.out.println("Broadcast: "+toIPString(broadcastAddress));
        System.out.println("HostMin: "+toIPString(hostMin));
        System.out.println("HostMax: "+toIPString(hostMax));
        System.out.println("Hosts/Net: "+hostsPerNet);
    }

    public static void validateIPAddress(String ipAddress) throws Exception {
        String[] parts=ipAddress.split("\\.");
        if(parts.length!=4) throw new Exception("IP address must contain four octets (e.g., 192.168.1.1)");
        for(String part:parts){
            try{
                int octet=Integer.parseInt(part.trim());
                if(octet<0||octet>255) throw new Exception("Each octet must be between 0 and 255");
            }catch(NumberFormatException e){
                throw new Exception("IP address octets must be valid integers between 0 and 255");
            }
        }
    }

    public static int[] parseIP(String ipAddress) {
        String[] parts=ipAddress.split("\\.");
        int[] octets=new int[4];
        for(int i=0;i<4;i++){
            octets[i]=Integer.parseInt(parts[i].trim());
        }
        return octets;
    }

    public static int[] calculateSubnetMask(int cidr) {
        int[] mask=new int[4];
        for(int i=0;i<cidr;i++){
            mask[i/8]|=(1<<(7-(i%8)));
        }
        return mask;
    }

    public static int calculateCIDR(int[] mask) {
        int cidr=0;
        for(int octet:mask){
            while(octet!=0){
                cidr+=octet&1;
                octet>>=1;
            }
        }
        return cidr;
    }

    public static int calculateHostsPerNet(int cidr) {
        if(cidr==32) return 1;
        return (int)Math.pow(2,(32-cidr))-2;
    }

    public static String toIPString(int[] octets) {
        return octets[0]+"."+octets[1]+"."+octets[2]+"."+octets[3];
    }
}
