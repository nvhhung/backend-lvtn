package provider;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Data

@Component
public class InetAddressProvider {
    private InetAddress inetAddress;

    public InetAddressProvider() throws UnknownHostException {
        this.inetAddress = InetAddress.getLocalHost();
    }
}