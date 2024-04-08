import java.io.*;

class Main {

  public static void main(String args[]) {

    File f = new File("dados/violoes.db");
    f.delete();

    Arquivo<Violao> arqVioloes;
    Violao l1 = new Violao( "Tagima", "Montana", 7);
    Violao l2 = new Violao( "Tagima", "Dalas", 4);
    Violao l3 = new Violao();
    int id1, id2;

    try {
      arqVioloes = new Arquivo<>("dados/violoes.db", Violao.class.getConstructor());

      id1 = arqVioloes.create(l1);
      System.out.println("Violao criado com o ID: " + id1);

      id2 = arqVioloes.create(l2);
      System.out.println("Violao criado com o ID: " + id2);

      if (arqVioloes.delete(id2))
        System.out.println("Violao de ID " + id2 + " excluido!");
      else
        System.out.println("Violao de ID " + id2 + " não encontrado!");

      l1.setModelo("Kansas");
      if (arqVioloes.update(l1))
        System.out.println("Violao de ID " + l1.getID() + " alterado!");
      else
        System.out.println("Violao de ID " + l1.getID() + " não encontrado!");

      if ((l3 = arqVioloes.read(id1)) != null)
        System.out.println(l3);
      else
        System.out.println("Violao de ID " + id1 + " não encontrado!");

      if ((l3 = arqVioloes.read(id2)) != null)
        System.out.println(l3);
      else
        System.out.println("Violao de ID " + id2 + " não encontrado!");
      arqVioloes.close();

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}