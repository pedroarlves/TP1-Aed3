import java.io.*;

class Main {

  public static void main(String args[]) {

    File f = new File("dados/violoes.db");
    f.delete();

    Arquivo<Violao> arqVioloes;
    Violao l1 = new Violao( "Tagima", "Montana", 7);
    Violao l2 = new Violao( "Tagima", "Dalas", 3);
    Violao p = new Violao();
    Violao l3 = new Violao("Gibson", "Fly", 2 );
    Violao l4 = new Violao("Tagima", "Kansas Mahogany", 5 );
    int id1, id2, id3, id4;

    try {
      arqVioloes = new Arquivo<>("dados/violoes.db", Violao.class.getConstructor());// criando arquivo que armazenara os dados

      id1 = arqVioloes.create(l1);// criando primeiro obj
      System.out.println("Violao criado com o ID: " + id1);

      id2 = arqVioloes.create(l2); // criando segundo obj
      System.out.println("Violao criado com o ID: " + id2);

      if (arqVioloes.delete(id2)) // excluindo segundo obj
        System.out.println("Violao de ID " + id2 + " excluido!");
      else
        System.out.println("Violao de ID " + id2 + " não encontrado!");

      id3 = arqVioloes.create(l3); // criando terceiro obj
      System.out.println("Violao criado com o ID: " + id3);

      id4 = arqVioloes.create(l4); // criando quarto obj
      System.out.println("Violao criado com o ID: " + id4);

      if (arqVioloes.delete(id4)) // excluindo quarto obj
        System.out.println("Violao de ID " + id4 + " excluido!");
      else
        System.out.println("Violao de ID " + id4 + " não encontrado!");

      l1.setModelo("Kansas Folk");
      if (arqVioloes.update(l1))// fazendo update do primeiro
        System.out.println("Violao de ID " + l1.getID() + " alterado!");
      else
        System.out.println("Violao de ID " + l1.getID() + " não encontrado!");

      if ((p = arqVioloes.read(id1)) != null)/// lendo o primeiro
        System.out.println(p);
      else
        System.out.println("Violao de ID " + id1 + " não encontrado!");

      if ((p = arqVioloes.read(id2)) != null)// tentando ler o segundo
        System.out.println(p);
      else
        System.out.println("Violao de ID " + id2 + " não encontrado!");
      arqVioloes.close();

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}