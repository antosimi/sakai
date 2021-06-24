package org.sakaiproject.user.impl;


import lombok.extern.slf4j.Slf4j;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.db.api.SqlService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DBFaceRecognitionService  {

    /** Table name for users. */
    protected String m_tableName = "FACE_REC";

    /** All fields. */
    protected String[] m_fieldNames = {"ID","USER_ID", "PHOTO",};


    private transient SqlService sqlService	= (SqlService) ComponentManager.get(SqlService.class.getName());

    public HashMap<Integer, String> readValues() {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        HashMap<Integer, String> dbFaces= new HashMap<>();
        try {
            con  = sqlService.borrowConnection();
            String sql = "SELECT * FROM FACE_REC";
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                Integer ceva1 = rs.getInt(1);
                String ceva2 = rs.getString(2);
                Blob   ceva3 = rs.getBlob(3);

                if(ceva3 != null) {
                    String blog_s = convertBlobToString(ceva3);
                    dbFaces.put(ceva1, blog_s);
                }
            }

        } catch (SQLException e) {
            log.error("SQL error occurred while retrieving user types: " + e.getMessage(), e);

        }finally{
        try{
            if(rs != null)
                rs.close();
        }
        catch (SQLException e) {
            log.error("SQL error occurred while retrieving user types: " + e.getMessage(), e);

        }finally{
            try{
                if(st != null)
                    st.close();
            }catch (SQLException e) {
                log.error("SQL error occurred while retrieving user types: " + e.getMessage(), e);
            } finally{
                try {
                    if (con != null)
                        con.close();
                }catch (SQLException e) {
                    log.error("SQL error occurred while retrieving user types: " + e.getMessage(), e);
                }
            }
        }
    }
        return dbFaces;
    }
    public void insertValues(String userId, String photo){
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con  = sqlService.borrowConnection();

            String query = "INSERT INTO  FACE_REC(USER_ID,PHOTO)"
                    + " VALUES (?,?)";


            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString(1,userId);

            //aici progratim din string in blob
            Blob blob = con.createBlob();
            blob.setBytes(1, photo.getBytes());
            preparedStmt.setBlob(2, blob);

            // execute the preparedstatement
            preparedStmt.executeUpdate();
            con.commit();



        } catch (SQLException e) {
            log.error("SQL error occurred while retrieving user types: " + e.getMessage(), e);

            }finally{
            try{
                if(rs != null)
                    rs.close();
            }
            catch (SQLException e) {
                log.error("SQL error occurred while retrieving user types: " + e.getMessage(), e);

            }finally{
                try{
                    if(st != null)
                        st.close();
                }catch (SQLException e) {
                    log.error("SQL error occurred while retrieving user types: " + e.getMessage(), e);
                } finally{
                    try {
                        if (con != null)
                            con.close();
                    }catch (SQLException e) {
                        log.error("SQL error occurred while retrieving user types: " + e.getMessage(), e);
                    }
                }
            }
        }
    }

    public HashMap<Integer, String> readValuesByEmail(String email) {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        HashMap<Integer, String> dbFaces= new HashMap<>();
        try {
            con  = sqlService.borrowConnection();
            String sql = "SELECT * FROM sakaidatabase.FACE_REC WHERE sakaidatabase.FACE_REC.EMAIL= '"+ email +"';";
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                Integer ceva1 = rs.getInt(1);
                String ceva2 = rs.getString(2);
                Blob   ceva3 = rs.getBlob(3);

                if(ceva3 != null) {
                    String blog_s = convertBlobToString(ceva3);
                    dbFaces.put(ceva1, blog_s);
                }
            }

        } catch (SQLException e) {
            log.error("SQL error occurred while retrieving user types: " + e.getMessage(), e);

        }finally{
            try{
                if(rs != null)
                    rs.close();
            }
            catch (SQLException e) {
                log.error("SQL error occurred while retrieving user types: " + e.getMessage(), e);

            }finally{
                try{
                    if(st != null)
                        st.close();
                }catch (SQLException e) {
                    log.error("SQL error occurred while retrieving user types: " + e.getMessage(), e);
                } finally{
                    try {
                        if (con != null)
                            con.close();
                    }catch (SQLException e) {
                        log.error("SQL error occurred while retrieving user types: " + e.getMessage(), e);
                    }
                }
            }
        }
        return dbFaces;
    }

    public String convertBlobToString(Blob blob){
        if(blob == null){
            return null;
        }
//        String result = "";
//        InputStream inStream = null;
//        try {
//            inStream = blob.getBinaryStream();
//            InputStreamReader inStreamReader = new InputStreamReader(inStream);
//            BufferedReader reader = new BufferedReader(inStreamReader );
//            StringBuffer buf = new StringBuffer();
//            while(reader.readLine()!=null){
//                result = reader.readLine();
//                buf.append(result);
//            }
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return result;


        //SOL 2 -- SOL ALTERNATIVA:

        byte[] bdata = new byte[0];
        String result = "";
        try {
            bdata = blob.getBytes(1, (int) blob.length());
            result = new String(bdata);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;

    }




}
