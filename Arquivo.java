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
    arq.seek(0); // vai pra posicao inicial do arquivo
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
    short melhorTam = -1;// tamanho do melhor espaco para guardar o objeto
    long endAtual;
    arq.seek(TAM_CABECALHO);
    while (arq.getFilePointer() < arq.length() ) {
      endAtual = arq.getFilePointer();// end atual do ponteiro de arquivo
      lapide = arq.readByte();// ler lapide atual
      tamTmp = arq.readShort();// let tamanho atual do objeto temporario
      //System.out.println("lapide: "+lapide+ " tamTmp: "+tamTmp+ " tamObj: "+tamObj + " melhorTam: "+melhorTam);
      if (lapide == ' ' || tamTmp < tamObj ) {// caso seja um espaco usado ou o obj novo nao caiba nesse espaco
          arq.seek(arq.getFilePointer() + tamTmp);// pular pro proximo obj
      }
      else if (melhorTam == -1 || tamTmp < melhorTam) {// caso seja a primeira lapide adequada encontrada, ou, essa lapide eh mais adequada
        melhorTam = tamTmp;
        endMelhorLapide = endAtual;
        arq.seek(arq.getFilePointer() + tamTmp );
      }
    }
    arq.seek(endMelhorLapide);// endereco mais adequado para colocar o novo objeto
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
    return obj.getID(); // retorna o id do objeto criado
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
    boolean result = false; // existencia do objeto 
    boolean testeTamID = false; // possibilidade de caber nos espaco atual do id
    T l = construtor.newInstance();
    byte[] ba;
    short tam;
    byte lapide;
    long endereco = arq.length(); 

    short tamMelhorLapide = -1; // tamanaho da lapide mais adequado para upar o no objeto
    long endMelhorLapide = arq.length();//endereco da lapide mais adequado para upar o no objeto
    long endID = arq.length();//endereco do espcdo do id 
    byte[] baObjAlter = objAlterado.toByteArray(); // dados do objeto novo em hexa/binario
    short tamObjAlter = (short) baObjAlter.length;// tamanho do obj novo

    arq.seek(TAM_CABECALHO); 
    while(arq.getFilePointer() < arq.length()  && testeTamID == false){// fazer enquanto fim do arquivo ou enquanto nao caber no espaco atual do id
      endereco = arq.getFilePointer(); // endereco da lapide atual
      lapide = arq.readByte(); // valor da lapide atual
      tam = arq.readShort(); // tam do objeto atual
      ba = new byte[tam]; // array de bytes
      arq.read(ba);// leitura do objeto atual
      l.fromByteArray(ba);// transformacao do obj em bytes para o objeto legivel
      if (lapide == '*' ) { // se id nao encontrado e espaco nao excluido
        if(result == true && tam >= tamObjAlter && (tam < tamMelhorLapide || tamMelhorLapide == -1) ){// se o obj existe e nao cabe o obj novo la, mas cabe aqui 
          tamMelhorLapide = tam; // tamanho da melhor lapide
          endMelhorLapide = endereco; // endereco da melhor lapide
        }
      }
      else if (l.getID() == objAlterado.getID()) { // objeto existe na lista
          endID = endereco;
          result = true; // id foi encontrado
          testeTamID = tam >= tamObjAlter; // cabe no espaco atual do id 
      }
    }

    if (result == true ) { // se o id existe
      if (testeTamID == true) { // se o espaco atual do id cabe
          arq.seek(endereco + 1 + 2);// ir para o espaco onde guarda o obj, tanto faz ser endID ou endereco, pois while finaliza caso caiba no espaco do id 
          arq.write(baObjAlter);//upando o objeto
      }
      else{
        arq.seek(endID);// ir para o endereco atual do id
        arq.write('*');// excluir o endereco atual do id
        arq.seek(endMelhorLapide);// ir para novo endereco do id
        arq.writeByte(' ');// escrever que eh espaco valido
        arq.writeShort(tamMelhorLapide == -1? tamObjAlter:tamMelhorLapide);// caso seja uma lapide escrever tamanho da obj da lapide, caso contrario escrever o do obj upado
        arq.write(baObjAlter);//escrever obj upado
      }
    }

    /* * /
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
      } 
      else {
        arq.skipBytes(tam);
      }
    }/* */
    return result;// retorna se o obj existe na lista
  }

  public void close() throws Exception {
    try {
        arq.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    
  }
}
