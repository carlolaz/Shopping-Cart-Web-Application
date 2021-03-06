package controller;

import model.dao.*;
import model.mo.*;
import services.config.Configuration;
import services.logservice.LogService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.dao.exception.DuplicatedObjectException;


public class ProductsManagement {

    private ProductsManagement(){

    }

    public static void view(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory;
        User loggedUser;
        String applicationMessage = null;

        Logger logger = LogService.getApplicationLogger();

        try{

            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            String searchMode = request.getParameter("searchMode");
            String searchString = request.getParameter("searchString");

            request.setAttribute("searchMode",searchMode);
            request.setAttribute("searchString",searchString);

            commonView(daoFactory, sessionDAOFactory, request);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("applicationMessage", null);
            request.setAttribute("viewUrl","productsManagement/view");

        } catch(Exception e){
            logger.log(Level.SEVERE, "Controller error",e);
            try{
                if(sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t){

            }
            throw new RuntimeException(e);
        } finally {
            try{
                if(sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t){

            }
        }

    }

    public static void delete(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        User loggedUser;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            String ISBN = request.getParameter("ISBN");
            String magazzino = request.getParameter("nomeMagazzino");
            ContenutoNelMagazzinoDAO contenutoNelMagazzinoDAO = daoFactory.getContenutoNelMagazzinoDAO();
            ContenutoNelMagazzino contenutoNelMagazzino = contenutoNelMagazzinoDAO.findByFumettoISBNRefAndMagazzinoRef(ISBN, magazzino);
            contenutoNelMagazzinoDAO.deleteFromWarehouse(contenutoNelMagazzino);

            commonView(daoFactory, sessionDAOFactory, request);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewUrl", "productsManagement/view");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }

    }

    public static void undelete(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        User loggedUser;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            String ISBN = request.getParameter("ISBN");
            String magazzino = request.getParameter("nomeMagazzino");
            ContenutoNelMagazzinoDAO contenutoNelMagazzinoDAO = daoFactory.getContenutoNelMagazzinoDAO();
            ContenutoNelMagazzino contenutoNelMagazzino = contenutoNelMagazzinoDAO.findByFumettoISBNRefAndMagazzinoRef(ISBN, magazzino);
            contenutoNelMagazzinoDAO.undeleteFromWarehouse(contenutoNelMagazzino);

            commonView(daoFactory, sessionDAOFactory, request);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewUrl", "productsManagement/view");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }

    }

    public static void insert(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory;
        User loggedUser;
        String applicationMessage = null;
        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            /* Setto i parametri */
            FumettoDAO fumettoDAO = daoFactory.getFumettoDAO();
            MagazzinoDAO magazzinoDAO = daoFactory.getMagazzinoDAO();
            ContenutoNelMagazzinoDAO contenutoNelMagazzinoDAO = daoFactory.getContenutoNelMagazzinoDAO();
            CentroVenditaDAO centroVenditaDAO = daoFactory.getCentroVenditaDAO();
            FornitoDaDAO fornitoDaDAO = daoFactory.getFornitoDaDAO();

            String nomeMagazzino = request.getParameter("nomeMagazzino");
            String nomeFornitore = request.getParameter("nomeFornitore");
            String ISBN = request.getParameter("ISBN");

            /* Richiesta di tutti i parametri */
            String titolo = request.getParameter("Titolo");
            String autore = request.getParameter("Autore");
            int numero = Integer.parseInt(request.getParameter("Numero"));
            String formato = request.getParameter("Formato");
            int quantita = Integer.parseInt(request.getParameter("Quantita"));
            String rilegatura = request.getParameter("Rilegatura");
            Float peso = Float.valueOf(request.getParameter("Peso"));
            Float prezzo = Float.valueOf(request.getParameter("Prezzo"));
            String bloccato = request.getParameter("Bloccato");
            String deleted = request.getParameter("Deleted");

            Magazzino magazzino = magazzinoDAO.findByNomeMagazzino(nomeMagazzino);
            CentroVendita centroVendita = centroVenditaDAO.findByNomeCentro(nomeFornitore);

            try {

                fumettoDAO.create(
                        ISBN,
                        titolo,
                        autore,
                        numero,
                        formato,
                        rilegatura,
                        prezzo,
                        peso,
                        bloccato);
                applicationMessage="Fumetto nuovo creato con successo.";

            } catch (DuplicatedObjectException e) {
                applicationMessage = "Fumetto già esistente!";
                logger.log(Level.INFO, "Tentativo di inserimento di prodotto già esistente.");
            }

            Fumetto fumetto = fumettoDAO.findByFumettoISBNNotYetCreated(ISBN);
            /*-----------------------------------------------------------------------------------------------*/

            try {
                contenutoNelMagazzinoDAO.create(
                        fumetto,
                        magazzino,
                        quantita,
                        deleted);
                applicationMessage=applicationMessage+"Fumetto nuovo creato con successo in magazzino.";

            }  catch (DuplicatedObjectException e) {
                applicationMessage = "Fumetto già esistente in magazzino!";
                logger.log(Level.INFO, "Tentativo di inserimento di prodotto già esistente in magazzino.");
            }

            /*-----------------------------------------------------------------------------------------------*/

            try {
                fornitoDaDAO.create(fumetto, centroVendita);
                applicationMessage=applicationMessage+"Fumetto nuovo creato con successo nella lista delle forniture.";
            } catch (DuplicatedObjectException e) {
                applicationMessage = "Fumetto già esistente nella lista delle forniture!";
                logger.log(Level.INFO, "Tentativo di inserimento di prodotto già fornito.");
            }

            commonView(daoFactory, sessionDAOFactory, request);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("viewUrl", "productsManagement/view");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }

    }

    public static void insertView(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory= null;
        User loggedUser;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewUrl", "productsManagement/insView");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }

    }

    public static void block(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        User loggedUser;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            if (sessionDAOFactory != null) {
                sessionDAOFactory.beginTransaction();
            }

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            String ISBN = request.getParameter("ISBN");
            FumettoDAO fumettoDAO = daoFactory.getFumettoDAO();
            Fumetto fumetto = fumettoDAO.findByFumettoISBN(ISBN);
            fumettoDAO.block(fumetto);
            String applicationMessage= "Fumetto "+ISBN +" bloccato.";

            /* Chiamo la common view */
            commonView(daoFactory, sessionDAOFactory, request);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("viewUrl", "productsManagement/view");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }

    }

    public static void unblock(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        User loggedUser;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            if (sessionDAOFactory != null) {
                sessionDAOFactory.beginTransaction();
            }

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            String ISBN = request.getParameter("ISBN");
            FumettoDAO fumettoDAO = daoFactory.getFumettoDAO();
            Fumetto fumetto = fumettoDAO.findByFumettoISBN(ISBN);
            fumettoDAO.unblock(fumetto);

            String applicationMessage;
            applicationMessage = "Fumetto "+ISBN+" sbloccato.";

            commonView(daoFactory, sessionDAOFactory, request);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("viewUrl", "productsManagement/view");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }

    }


    public static void modifyView(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory= null;
        User loggedUser;
        DAOFactory daoFactory = null;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            String ISBN = request.getParameter("ISBN");
            String nomeMagazzino = request.getParameter("nomeMagazzino");
            String centroVendita = request.getParameter("nomeFornitore");
            FumettoDAO fumettoDAO = daoFactory.getFumettoDAO();
            ContenutoNelMagazzinoDAO contenutoNelMagazzinoDAO = daoFactory.getContenutoNelMagazzinoDAO();
            FornitoDaDAO fornitoDaDAO = daoFactory.getFornitoDaDAO();

            ContenutoNelMagazzino contenutoNelMagazzino = contenutoNelMagazzinoDAO.findByFumettoISBNRefAndMagazzinoRef(ISBN, nomeMagazzino);
            FornitoDa fornitoDa = fornitoDaDAO.findByFumettoISBNandCentroVenditaRef(ISBN,centroVendita);
            Fumetto fumetto = fumettoDAO.showFumetto(ISBN);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("fumetto",fumetto);
            request.setAttribute("contenutoNelMagazzino",contenutoNelMagazzino);
            request.setAttribute("fornitoDa",fornitoDa);
            request.setAttribute("viewUrl", "productsManagement/insView");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }

    }

    public static void modify(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory;
        String applicationMessage = null;
        User loggedUser;
        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            /* Richiesta dei parametri essenziali */
            String ISBN = (request.getParameter("ISBN"));
            String nomeMagazzino = request.getParameter("nomeMagazzino");
            String nomeFornitore = request.getParameter("nomeFornitore");

            FumettoDAO fumettoDAO = daoFactory.getFumettoDAO();
            Fumetto fumetto = fumettoDAO.findByFumettoISBN(ISBN);
            ContenutoNelMagazzinoDAO contenutoNelMagazzinoDAO = daoFactory.getContenutoNelMagazzinoDAO();
            ContenutoNelMagazzino contenutoNelMagazzino = contenutoNelMagazzinoDAO.findByFumettoISBNRefAndMagazzinoRef(ISBN, nomeMagazzino);
            FornitoDaDAO fornitoDaDAO = daoFactory.getFornitoDaDAO();
            FornitoDa fornitoDa = fornitoDaDAO.findByFumettoISBNandCentroVenditaRef(ISBN,nomeFornitore);

            fumetto.setTitolo(request.getParameter("Titolo"));
            fumetto.setAutore(request.getParameter("Autore"));
            fumetto.setNumero(Integer.parseInt(request.getParameter("Numero")));
            fumetto.setFormato(request.getParameter("Formato"));
            fumetto.setRilegatura(request.getParameter("Rilegatura"));
            fumetto.setPrezzo(Float.parseFloat(request.getParameter("Prezzo")));
            fumetto.setPeso(Float.parseFloat(request.getParameter("Peso")));
            fumetto.setBlocked((request.getParameter("Bloccato")));

            contenutoNelMagazzino.setQuantita(Integer.parseInt(request.getParameter("Quantita")));
            contenutoNelMagazzino.setDeleted(request.getParameter("Deleted"));

            /* Inserisco il nuovo fumetto nella tabella di MySQL in : */
            /* La tabella dei fumetti, la tabella della fornitura, e la tabella di disponibilita' di magazzino effettiva */
            try {
                fumettoDAO.update(fumetto);
            } catch (DuplicatedObjectException e) {
                applicationMessage = "Fumetto già esistente.";
                logger.log(Level.INFO, "Tentativo di inserimento di un fumetto già esistente.");
            }

            try {
                contenutoNelMagazzinoDAO.updateWarehouse(contenutoNelMagazzino);
            } catch (DuplicatedObjectException e) {
                applicationMessage = "Fumetto già esistente in magazzino.";
                logger.log(Level.INFO, "Tentativo di inserimento di un fumetto già esistente in magazzino.");
            }

            commonView(daoFactory, sessionDAOFactory, request);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("applicationMessage", applicationMessage);
            request.setAttribute("viewUrl", "productsManagement/view");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }

    }

    public static void viewDetails(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        User loggedUser;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            String ISBN = (request.getParameter("ISBN"));
            String nomeFornitore = request.getParameter("nomeFornitore");
            String nomeMagazzino = request.getParameter("nomeMagazzino");
            FumettoDAO fumettoDAO = daoFactory.getFumettoDAO();
            FornitoDaDAO fornitoDaDAO = daoFactory.getFornitoDaDAO();
            ContenutoNelMagazzinoDAO contenutoNelMagazzinoDAO = daoFactory.getContenutoNelMagazzinoDAO();
            Fumetto fumetto = fumettoDAO.showFumetto(ISBN);
            FornitoDa fornitoDa = fornitoDaDAO.findByFumettoISBNandCentroVenditaRef(ISBN,nomeFornitore);
            ContenutoNelMagazzino contenutoNelMagazzino = contenutoNelMagazzinoDAO.findByFumettoISBNRefAndMagazzinoRef(ISBN,nomeMagazzino);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("fumetto",fumetto);
            request.setAttribute("fornitoDa",fornitoDa);
            request.setAttribute("contenutoNelMagazzino",contenutoNelMagazzino);
            request.setAttribute("viewUrl", "productsManagement/viewDetails");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }


    public static void addQuantityToWarehouse(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        User loggedUser;
        String applicationMessage = null;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            String ISBN = request.getParameter("ISBN");
            String nomeFornitore = request.getParameter("nomeFornitore");
            String magazzino = request.getParameter("nomeMagazzino");
            FumettoDAO fumettoDAO = daoFactory.getFumettoDAO();
            FornitoDaDAO fornitoDaDAO = daoFactory.getFornitoDaDAO();
            FornitoDa fornitoDa = fornitoDaDAO.findByFumettoISBNandCentroVenditaRef(ISBN,nomeFornitore);

            Fumetto fumetto  = fumettoDAO.findByFumettoISBN(ISBN);

            ContenutoNelMagazzinoDAO contenutoNelMagazzinoDAO = daoFactory.getContenutoNelMagazzinoDAO();
            ContenutoNelMagazzino contenutoNelMagazzino = contenutoNelMagazzinoDAO.findByFumettoISBNRefAndMagazzinoRef(ISBN, magazzino);

            try {
                contenutoNelMagazzinoDAO.addQuantityToWarehouse(contenutoNelMagazzino);
            } catch(RuntimeException e){
                applicationMessage = "Errore nel modificare la quantità di magazzino.";
                logger.log(Level.INFO, "Errore nel modificare la quantità di magazzino.");
            }


            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("contenutoNelMagazzino",contenutoNelMagazzino);
            request.setAttribute("fornitoDa",fornitoDa);
            request.setAttribute("fumetto",fumetto);
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("viewUrl", "productsManagement/insView");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }

    }

    public static void removeQuantityFromWarehouse(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        User loggedUser;
        String applicationMessage = null;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            String ISBN = request.getParameter("ISBN");
            String nomeFornitore = request.getParameter("nomeFornitore");
            String magazzino = request.getParameter("nomeMagazzino");
            FumettoDAO fumettoDAO = daoFactory.getFumettoDAO();
            FornitoDaDAO fornitoDaDAO = daoFactory.getFornitoDaDAO();
            FornitoDa fornitoDa = fornitoDaDAO.findByFumettoISBNandCentroVenditaRef(ISBN,nomeFornitore);
            Fumetto fumetto  = fumettoDAO.findByFumettoISBN(ISBN);
            ContenutoNelMagazzinoDAO contenutoNelMagazzinoDAO = daoFactory.getContenutoNelMagazzinoDAO();
            ContenutoNelMagazzino contenutoNelMagazzino = contenutoNelMagazzinoDAO.findByFumettoISBNRefAndMagazzinoRef(ISBN, magazzino);

            try{
                /* Se è presente nel magazzino con quantità maggiore di 0, tolgo 1, sennò non modifico. */
                if(contenutoNelMagazzinoDAO.getAvailability(ISBN,magazzino)>0)
                    contenutoNelMagazzinoDAO.removeQuantityFromWarehouse(contenutoNelMagazzino);

            } catch(RuntimeException e){
                applicationMessage = "Errore nel modificare la quantità di magazzino.";
                logger.log(Level.INFO, "Errore nel modificare la quantità di magazzino.");
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("fumetto",fumetto);
            request.setAttribute("fornitoDa",fornitoDa);
            request.setAttribute("contenutoNelMagazzino",contenutoNelMagazzino);
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("viewUrl", "productsManagement/insView");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }

    }


    public static void addToCart(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        User loggedUser;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            String ISBN = request.getParameter("ISBN");
            UserDAO userDAO = daoFactory.getUserDAO();
            FumettoDAO fumettoDAO = daoFactory.getFumettoDAO();
            CarrelloDAO carrelloDAO = daoFactory.getCarrelloDAO();

            User user = userDAO.findByUsername(loggedUser.getUsername());
            Fumetto fumetto = fumettoDAO.findByFumettoISBN(ISBN);

            /* Aggiungo il prodotto al carrello */
            carrelloDAO.addToCart(fumetto,user);

            commonView(daoFactory, sessionDAOFactory, request);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn", true);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewUrl", "productsManagement/view");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }

    }

    private static void commonView(DAOFactory daoFactory, DAOFactory sessionDAOFactory, HttpServletRequest request) throws FileNotFoundException {

        UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
        User loggedUser = sessionUserDAO.findLoggedUser();

        /* DAO */
        FumettoDAO fumettoDAO = daoFactory.getFumettoDAO();
        ContenutoNelMagazzinoDAO contenutoNelMagazzinoDAO = daoFactory.getContenutoNelMagazzinoDAO();
        FornitoDaDAO fornitoDaDAO = daoFactory.getFornitoDaDAO();

        ArrayList<Fumetto> fumetti = new ArrayList<>();
        ArrayList<ContenutoNelMagazzino> contenutoNelMagazzinoArrayList = new ArrayList<>();
        ArrayList<FornitoDa> fornitoDaArrayList = new ArrayList<>();
        String sm = request.getParameter("searchMode");
        String ss = request.getParameter("searchString");

        String searchMode = "";
        String searchString = "";

        if((ss!=null)&&(sm!=null)) {
            searchMode = sm.toUpperCase();
            searchString = ss;
        }
        
        File file = new File("C:\\Users\\Carlo\\IdeaProjects\\Fumetti\\web\\productImages");
        ArrayList<File> fileImages = new ArrayList<>();
        String[] fileList = file.list();
        HashMap<String,File> fileImagesNamesHashMap = new HashMap<>();

        assert fileList != null;
        int index = 0;
        /* An array List of pathnames */
        for(index = 0; index<fileList.length; index+=1){
            String newFileListName = "C:\\Users\\Carlo\\IdeaProjects\\Fumetti\\web\\productImages\\"+fileList[index];
            fileImages.add(index, new File(newFileListName));
            fileImagesNamesHashMap.put(fileList[index],fileImages.get(index));
        }

        /* Se sono admin voglio vedere anche i prodotti che ho bloccato */
        boolean isAdmin = false;
        if(loggedUser!=null) {
            if (loggedUser.getAdmin().equals("Y")) isAdmin = true;
        }

        /* Ricerca */
        if ((!(searchMode.equals("")))) {
            if(!(searchString.equals(""))) {
                switch (searchMode) {
                    case "TITOLO":
                    case "AUTORE":
                    case "NUMERO":
                        if(isAdmin) {
                            fumetti = fumettoDAO.findBy(searchMode, searchString);
                            contenutoNelMagazzinoArrayList = contenutoNelMagazzinoDAO.findByCNM(searchMode, searchString);
                            fornitoDaArrayList = fornitoDaDAO.findBy(searchMode, searchString);
                        }
                        else {
                            fumetti = fumettoDAO.findByUnblocked(searchMode, searchString);
                            contenutoNelMagazzinoArrayList = contenutoNelMagazzinoDAO.findByCNMUnblocked(searchMode, searchString);
                            fornitoDaArrayList = fornitoDaDAO.findByUnblocked(searchMode, searchString);
                        }
                        break;
                    default:
                        break;
                }
            }
            else {
                if(isAdmin) {
                    fumetti = fumettoDAO.findBy(searchMode, null);
                    contenutoNelMagazzinoArrayList = contenutoNelMagazzinoDAO.findByCNM(searchMode, null);
                    fornitoDaArrayList = fornitoDaDAO.findBy(searchMode, null);

                }
                else {
                    fumetti = fumettoDAO.findByUnblocked(searchMode, null);
                    contenutoNelMagazzinoArrayList = contenutoNelMagazzinoDAO.findByCNMUnblocked(searchMode, null);
                    fornitoDaArrayList = fornitoDaDAO.findByUnblocked(searchMode, null);
                }
            }
        } else if(!searchString.equals("")){
            if(isAdmin) {
                fumetti = fumettoDAO.freeSearch(searchString);
                contenutoNelMagazzinoArrayList = contenutoNelMagazzinoDAO.freeSearchCNM(searchString);
                fornitoDaArrayList = fornitoDaDAO.freeSearch(searchString);
            }
            else {
                fumetti = fumettoDAO.freeSearchUnblocked(searchString);
                contenutoNelMagazzinoArrayList = contenutoNelMagazzinoDAO.freeSearchCNMUnblocked(searchString);
                fornitoDaArrayList = fornitoDaDAO.freeSearchUnblocked(searchString);
            }
        } else {
            if(isAdmin) {
                fumetti = fumettoDAO.findAllFumetti();
                contenutoNelMagazzinoArrayList = contenutoNelMagazzinoDAO.findAllContenutoNelMagazzino();
                fornitoDaArrayList = fornitoDaDAO.findAllFornitoDa();
            }
            else {
                fumetti = fumettoDAO.findAllUnblockedFumetti();
                contenutoNelMagazzinoArrayList = contenutoNelMagazzinoDAO.findAllContenutoNelMagazzinoUnblocked();
                fornitoDaArrayList = fornitoDaDAO.findAllFornitoDaUnblocked();
            }
        }

        request.setAttribute("fumetti",fumetti);
        request.setAttribute("contenutoNelMagazzinoArrayList",contenutoNelMagazzinoArrayList);
        request.setAttribute("fornitoDaArrayList",fornitoDaArrayList);
        request.setAttribute("fileImagesNamesHashMap",fileImagesNamesHashMap)

    }

}
