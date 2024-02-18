package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Card;
import model.Player;

public class DaoImpl implements Dao{
	private Connection conn;
	private final static String HOST = "localhost";
	private final static Integer PORT = 3306;
	private final static String DBNAME = "uno";
	private final static String DBUSER = "root";
	private final static String DBPWD = "neil0502";
	public static final String GET_PERSONA_BY_ID = "select * from player where user = ? and password = ?";

	@Override
	public void connect() throws SQLException {
		// TODO Auto-generated method stub
			try {
				System.out.println("Estableciendo conexión...");
				conn = DriverManager.getConnection("jdbc:mysql://"+HOST+":"+PORT + "/" + DBNAME, DBUSER, DBPWD);
				System.out.println("Conexión establecida...");
			} catch (SQLException e) {
				System.err.println("[ERROR] No se ha podido establecer conexión");
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
	}

	@Override
	public void disconnect() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getLastIdCard(int playerId) throws SQLException {
		// TODO Auto-generated method stub
		int lastId = 0;
        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT ifnull(MAX(idcard), 0) + 1 AS lastId FROM card WHERE id_player = ?")) {
            preparedStatement.setInt(1, playerId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    lastId = resultSet.getInt("lastId");
                }
            }
        }
        return lastId;
    }

	@Override
	public Card getLastCard() throws SQLException {
		// TODO Auto-generated method stub
		String query = "SELECT idcard FROM game WHERE id =(SELECT MAX(id) from game";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String number = resultSet.getString("number");
                String color = resultSet.getString("color");
                int playerId = resultSet.getInt("playerId");
                return new Card(id, number, color, playerId);
            }
	}
		return null;
	}

	@Override
	public Player getPlayer(String user, String pass) throws SQLException {
		// TODO Auto-generated method stub
		String select = GET_PERSONA_BY_ID;
    	Player player = null;   	
    	try (PreparedStatement ps = conn.prepareStatement(select)) { 
    	  	ps.setString(1,user);
    	  	ps.setString(2, pass);
    	  	System.out.println(ps.toString());
            try (ResultSet rs = ps.executeQuery()) {
            	if (rs.next()) {
            		player = new Player("dinan");
            		player.setId(rs.getInt("idplayer"));
	                player.setName(rs.getString("name"));
	                player.setGames(0);
	                player.setVictories(0);
            	}
            }
        }
		return player;
	}

	@Override
	public ArrayList<Card> getCards(int playerId) throws SQLException {
		// TODO Auto-generated method stub
		ArrayList<Card> cards = new ArrayList<>();
	    String query = "SELECT * FROM card left join game ON card.idcard = game.id_card WHERE id_player = ? and game.idgame is null";
	    try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
	        preparedStatement.setInt(1, playerId);
	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            while (resultSet.next()) {
	                int id = resultSet.getInt("idcard");
	                String number = resultSet.getString("number");
	                String color = resultSet.getString("color");
	                cards.add(new Card(id, number, color, playerId));
	            }
	        }
	    }
	    return cards;
	}

	@Override
	public Card getCard(int cardId) throws SQLException {
		// TODO Auto-generated method stub
        return null;
	}

	@Override
	public void saveGame(Card card) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveCard(Card card) throws SQLException {
		// TODO Auto-generated method stub
		 String query = "INSERT INTO card (number, color, id_player) VALUES (?, ?, ?)";    
	        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
	            pstmt.setString(1, card.getNumber());
	            pstmt.setString(2, card.getColor());
	            pstmt.setInt(3, card.getPlayerId());
	            pstmt.executeUpdate();
	        }
	    }

	@Override
	public void deleteCard(Card card) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearDeck(int playerId) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addVictories(int playerId) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addGames(int playerId) throws SQLException {
		// TODO Auto-generated method stub
		
	}
	

}
