import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;

public class Arquivo<T extends Registro> {

  private static int TAM_CABECALHO = 4;
  RandomAccessFile arq;
  String nomeArquivo = "";
  Constructor<T> construtor;

  public Arquivo(String na, Constructor<T> c) throws Exception {
    this.nomeArquivo = na;
    this.construtor = c;
    arq = new RandomAccessFile(na, "rw");
    if (arq.length() < TAM_CABECALHO) {
      arq.seek(0);
      arq.writeInt(0);
    }
  }

  public int create(T obj) throws Exception {
    arq.seek(0);
    int ultimoID = arq.readInt();
    ultimoID++;
    arq.seek(0);
    arq.writeInt(ultimoID);
    obj.setID(ultimoID);


    byte[] ba = obj.toByteArray();//dados do objeto
    short tamObj = (short) ba.length;//tamanho do objeto

    byte lapide; // byte que indica se lapide esta marcado
    short tamTmp;// tamanho so espaco que vai ser reutilizado
    long endMelhorLapide = arq.length(); // endereco da lapide mais adequada
    short melhorTam = -1;
    long endAtual;
    arq.seek(TAM_CABECALHO);
    while (arq.getFilePointer() < arq.length() ) {
      endAtual = arq.getFilePointer();
      lapide = arq.readByte();
      tamTmp = arq.readShort();
      ///System.out.println("lapide: "+lapide+ " tamTmp: "+tamTmp+ " tamObj: "+tamObj + " melhorTam: "+melhorTam);
      if (lapide == ' ' || tamTmp < tamObj ) {
          arq.seek(arq.getFilePointer() + tamTmp);
      }
      else if (melhorTam == -1 || tamTmp < melhorTam) {
        melhorTam = tamTmp;
        endMelhorLapide = endAtual;
        arq.seek(arq.getFilePointer() + tamTmp );
      }
    }
    System.out.println("teste");
    arq.seek(endMelhorLapide);// endereco mais adequado para colocar o novo objeto
    System.out.println("teste");
    arq.write(' '); // lápide
    arq.writeShort(melhorTam == -1?tamObj:melhorTam);// tamanho do objeto
    arq.write(ba);//objeto

/* * /
    long endLapide;
    long endObj;
    byte lapide;
    short tamTmp;
    int teste = 0;
    while ( arq.getFilePointer() < arq.length() && teste == 0  ) {
        endLapide = arq.getFilePointer();
        lapide = arq.readByte();
        tamTmp = arq.readShort();
        endObj = arq.getFilePointer() + 1;
        if (lapide == ' ' || tamTmp > tamObj ) {
            arq.seek(endObj+ tamTmp);
        }
        else{
            teste = 1;
            arq.seek(endLapide);
            arq.write(' '); // lápide
            arq.seek(endObj);
            arq.write(ba);
        }
    }
    if (teste == 0) {
        arq.seek(arq.length());
        arq.write(' '); // lápide
        arq.writeShort(tamObj);
        arq.write(ba);
    }
/* */
    return obj.getID();
  }


  public T read(int id) throws Exception {
    T v = construtor.newInstance();
    byte[] ba;
    short tam;
    byte lapide;

    arq.seek(TAM_CABECALHO);
    while (arq.getFilePointer() < arq.length()) {

      lapide = arq.readByte();
      tam = arq.readShort();
      if (lapide == ' ') {
        ba = new byte[tam];
        arq.read(ba);
        v.fromByteArray(ba);
        if (v.getID() == id)
          return v;
      } else {
        arq.skipBytes(tam);
      }
    }
    return null;
  }

  public boolean delete(int id) throws Exception {
    T l = construtor.newInstance();
    byte[] ba;
    short tam;
    byte lapide;
    long endereco;

    arq.seek(TAM_CABECALHO);
    while (arq.getFilePointer() < arq.length()) {

      endereco = arq.getFilePointer();
      lapide = arq.readByte();
      tam = arq.readShort();
      if (lapide == ' ') {
        ba = new byte[tam];
        arq.read(ba);
        l.fromByteArray(ba);
        if (l.getID() == id) {
          arq.seek(endereco);
          arq.write('*');
          return true;
        }
      } else {
        arq.skipBytes(tam);
      }
    }
    return false;
  }

  public boolean update(T objAlterado) throws Exception {
    T l = construtor.newInstance();
    byte[] ba;
    short tam;
    byte lapide;
    long endereco;

    arq.seek(TAM_CABECALHO);
    while (arq.getFilePointer() < arq.length()) {

      endereco = arq.getFilePointer();
      lapide = arq.readByte();
      tam = arq.readShort();
      if (lapide == ' ') {
        ba = new byte[tam];
        arq.read(ba);
        l.fromByteArray(ba);
        if (l.getID() == objAlterado.getID()) {
          byte[] ba2 = objAlterado.toByteArray();
          short tam2 = (short) ba2.length;
          if (tam2 <= tam) {
            arq.seek(endereco + 1 + 2);
            arq.write(ba2);
          } else {
            arq.seek(endereco);
            arq.write('*');
            
            arq.seek(arq.length());
            arq.write(' ');
            arq.writeShort(tam2);
            arq.write(ba2);
          }
          return true;
        }
      } else {
        arq.skipBytes(tam);
      }
    }
    return false;
  }

  public void close() throws Exception {
    try {
        arq.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    
  }
}
