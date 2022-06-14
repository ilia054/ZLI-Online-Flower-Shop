package Entities;
/**
*This class is a class that stores the connect clients to our server 
*each time a client connects\disconnects to our server we will display his IP,Host,Connection Status
*so we need to save the Clients data with this Entity
 * @author Ilya Lev
 */
public class ConnectedClient {
	@Override
	public String toString() {
		return "[ip=" + ip + ", host=" + host + ", status=" + status + "]";
	}
	private String ip;
	private String host;
	private String status;

	public ConnectedClient(String ip, String host, String status) {

		this.ip = ip;
		this.host = host;
		this.status = status;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getIp() {
		return ip;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getHost() {
		return host;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatus() {
		return status;
	}

	

}
