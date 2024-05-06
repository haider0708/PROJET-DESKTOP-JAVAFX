    /*
     * To change this license header, choose License Headers in Project Properties.
     * To change this temlivreure file, choose Tools | Temlivreures
     * and open the temlivreure in the editor.
     */
    package models;

    /**
     *
     * @author asus
     */
    public class commande extends livreur {
        private int id_commande;
        private double rating;

    private String nom_client,addresse_client,numero_client;
    public int id;
    public livreur livreur;

        public commande() {
        }

        public commande(int id_commande, int id, String nom_client, String addresse_client, String numero_client) {
            this.id_commande = id_commande;


            this.id = id;
            this.nom_client = nom_client;
            this.addresse_client = addresse_client;

            this.numero_client = numero_client;


        }
        public commande(int id ,String nom_client, String addresse_client, String numero_client, Double rating) {


            this.id = id;
            this.rating = rating;
            this.nom_client = nom_client;
            this.addresse_client = addresse_client;
            this.numero_client = numero_client;


        }

        public commande(int id_commande, int id, livreur livreur, String nom_client, String addresse_client, String numero_client) {
            this.id_commande = id_commande;


            this.id = id;
            this.livreur = livreur;
            this.nom_client = nom_client;
            this.addresse_client = addresse_client;

            this.numero_client = numero_client;


        }
        public commande(int id, String nom_client, String addresse_client, String numero_client) {



            this.id = id;

            this.nom_client = nom_client;
            this.addresse_client = addresse_client;

            this.numero_client = numero_client;


        }

        public int getId_commande() {
            return id_commande;
        }



        public double getRating() {
            return rating;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }

        public int getId() {
            return id;
        }

        public livreur getlivreur() {
            return livreur;
        }

        public void setId_commande(int id_commande) {
            this.id_commande = id_commande;
        }





        public void setId(int id) {
            this.id = id;
        }
        public void setNom_client(String nom_client) {
            this.nom_client = nom_client;
        }
        public String getNom_client() {
            return nom_client;
        }


        public void setAddresse_client(String addresse_client) {
            this.addresse_client = addresse_client;
        }
        public String getAddresse_client() {
            return addresse_client;
        }
        public void setNumero_client(String numero_client) {
            this.numero_client = numero_client;
        }
        public String getNumero_client() {
            return numero_client;
        }


        public void setLivreur(livreur livreur) {
            this.livreur = livreur;
        }

        @Override
        public String toString() {
            return "commande{" + "id_commande=" + id_commande +  ", id=" + id +  ", nom_client=" + nom_client + '}';
        }







    }
