import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Violao implements Registro {
    // atributos
    private int ID;
    private String marca;
    private String modelo;
    private int qntd;

    // Construtores

    public Violao() {
        ID = -1;
        marca = "";
        modelo = "";
        qntd = -1;
    }

    public Violao(int iD, String marca, String modelo, int qntd) {
        ID = iD;
        this.marca = marca;
        this.modelo = modelo;
        this.qntd = qntd;
    }

    public Violao(String marca, String modelo, int qntd) {
        ID = -1;
        this.marca = marca;
        this.modelo = modelo;
        this.qntd = qntd;
    }

    // Getters/setters
    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getQntd() {
        return qntd;
    }

    public void setQntd(int qntd) {
        this.qntd = qntd;
    }

    // conversor e descoversor de dados em bytes
    @Override
    public byte[] toByteArray() throws Exception {
        ByteArrayOutputStream ba_out = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(ba_out);
        dos.writeInt(this.ID);
        dos.writeUTF(this.marca);
        dos.writeUTF(this.modelo);
        dos.writeInt(this.qntd);
        return ba_out.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws Exception {
        ByteArrayInputStream ba_in = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(ba_in);
        this.ID = dis.readInt();
        this.marca = dis.readUTF();
        this.modelo = dis.readUTF();
        this.qntd = dis.readInt();
    }

    // toString
    @Override
    public String toString() {
        return "Violao [ID=" + ID + ", marca=" + marca + ", modelo=" + modelo + ", qntd=" + qntd + "]";
    }

}
