import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.jupiter.api.Test;

public class main {

	@Test
	public static void main(String[] args) throws Exception {
		// TODO 自动生成的方法存根
		InputStream is = main.class.getClassLoader().getResourceAsStream("jdbc.properties");

		Properties pros = new Properties();
		pros.load(is);

		String user = pros.getProperty("user");
		String password = pros.getProperty("password");
		String url = pros.getProperty("url");
		String driverClass = pros.getProperty("driverClass");

		// load the driver
		Class.forName(driverClass);

		// get connection
		Connection conn = DriverManager.getConnection(url, user, password);
		// System.out.println(conn);

		// Q1

		System.out.println("Q1:How much money did the company make in 2019, broken down by Movies and TV in 2019?\r\n");

		String sql = "select sum(buy_price) + (select sum(rent_price)\r\n"
				+ "from products natural join movie natural join rent_product ) as movies_earn\r\n"
				+ "from products  natural join movie natural join buy_product;\r\n";

		PreparedStatement ps = conn.prepareStatement(sql);

		// execute and return a resultSet
		ResultSet resultSet = ps.executeQuery();

		// deal with resultSet
		if (resultSet.next()) {

			int movies_earn = resultSet.getInt(1);

			System.out.println("Movies made " + movies_earn + "RMB in 2019");

		}

		String sql1 = "select sum(buy_price) + (select sum(rent_price)\r\n"
				+ "from products natural join rent_product \r\n" + "where p_id>40 and p_id<291\r\n"
				+ ") as tv_show_earn\r\n" + "from products natural join buy_product \r\n"
				+ "where p_id>40 and p_id<291;\r\n" + "";

		PreparedStatement ps1 = conn.prepareStatement(sql1);

		// execute and return a resultSet
		ResultSet resultSet1 = ps1.executeQuery();
		if (resultSet1.next()) {

			int TV_shows_earn = resultSet1.getInt(1);

			System.out.println("TV shows made " + TV_shows_earn + "RMB in 2019");

		}

		String sql2 = "select sum(buy_price) + (select sum(rent_price) from products natural join rent_product)\r\n"
				+ "as total_money\r\n" + "from products natural join  buy_product;\r\n";

		PreparedStatement ps2 = conn.prepareStatement(sql2);

		// execute and return a resultSet
		ResultSet resultSet2 = ps2.executeQuery();
		if (resultSet2.next()) {

			int total_earn = resultSet2.getInt(1);

			System.out.println("company made " + total_earn + "RMB in total in 2019");

		}

		System.out.println();
		// Q2
		System.out
				.println("Q2: How much money did the company make in 2019, broken down by purchases and rentals?\r\n");

		String sql3 = "select sum(buy_price)\r\n" + "from products natural join buy_product;\r\n";

		PreparedStatement ps3 = conn.prepareStatement(sql3);

		// execute and return a resultSet
		ResultSet resultSet3 = ps3.executeQuery();
		if (resultSet3.next()) {

			int buy_earn = resultSet3.getInt(1);

			System.out.println("company made " + buy_earn + "RMB by saling items in 2019");

		}

		String sql4 = "select sum(rent_price)\r\n" + "from products natural join rent_product;\r\n";

		PreparedStatement ps4 = conn.prepareStatement(sql4);

		// execute and return a resultSet
		ResultSet resultSet4 = ps4.executeQuery();
		if (resultSet4.next()) {

			int rent_earn = resultSet4.getInt(1);

			System.out.println("company made " + rent_earn + "RMB by renting in 2019");

		}

		// Q3
		System.out.println();
		System.out.println("Q3: Which movie was watched the highest number of times?");

		String sql5 = "SELECT title, count( * ) AS count  \r\n" + "FROM watch_product natural join products\r\n"
				+ "where p_id<41\r\n" + "GROUP BY p_id\r\n" + "ORDER BY count DESC  \r\n" + "LIMIT 1;";

		PreparedStatement ps5 = conn.prepareStatement(sql5);

		// execute and return a resultSet
		ResultSet resultSet5 = ps5.executeQuery();
		if (resultSet5.next()) {

			String best_movie = resultSet5.getString(1);
			int time = resultSet5.getInt(2);

			System.out.println(
					"The hottest movie in 2019 is " + best_movie + ", it has been watched for " + time + " times");

		}

		// Q4
		System.out.println();
		System.out.println("Q4: Which user spent the most amount of time watching TV and movies in 2019?");

		String sql6 = "SELECT c_id, given_name, family_name, count( * ) AS count  \r\n"
				+ "FROM watch_product natural join customers\r\n" + "GROUP BY c_id\r\n" + "ORDER BY count DESC  \r\n"
				+ "LIMIT 1;";

		PreparedStatement ps6 = conn.prepareStatement(sql6);

		// execute and return a resultSet
		ResultSet resultSet6 = ps6.executeQuery();
		if (resultSet6.next()) {

			int id = resultSet6.getInt(1);
			String given_name = resultSet6.getString(2);
			String family_name = resultSet6.getString(3);

			System.out.println("User id " + id + " name " + given_name + " " + family_name
					+ " spent the most amount of time watching TV and movies in 2019");

		}

		// Q5
		System.out.println();
		System.out.println("Q5: Which TV show had the lowest average price for each episode?");

		String sql7 = "\r\n" + "SELECT episode.title, avg(rent_price+buy_price) as AVG\r\n"
				+ "FROM  episode ,products \r\n" + "WHERE products.p_id = episode.p_id \r\n"
				+ "GROUP BY episode.title \r\n" + "ORDER BY AVG ASC\r\n" + "LIMIT 1; \r\n";

		PreparedStatement ps7 = conn.prepareStatement(sql7);

		// execute and return a resultSet
		ResultSet resultSet7 = ps7.executeQuery();
		if (resultSet7.next()) {

			String name = resultSet7.getString(1);
			int minprice = resultSet7.getInt(2);

			System.out.println(
					"TV show " + name + " has the lowest average price of total rent and buy price " + minprice);

		}

		// Q6
		System.out.println();
		System.out.println("Q6:  List all of the movies that can be bought for less than 50 RMB.");

		String sql8 = "SELECT title from products natural join movie where buy_price < 50;";

		System.out.println("title:");
		PreparedStatement ps8 = conn.prepareStatement(sql8);

		// execute and return a resultSet
		ResultSet resultSet8 = ps8.executeQuery();
		while (resultSet8.next()) {

			String title = resultSet8.getString(1);
			System.out.println(String.format("| %-80s |", title));

		}

		// Q7
		System.out.println();
		System.out.println("Q7: What episodes of any TV show have never been watched?");

		String sql9 = "Select products.title, episode.title \r\n" + "from products, episode, watch_product\r\n"
				+ "where products.p_id = episode.p_id and episode.p_id = watch_product.p_id and products.p_id \r\n"
				+ "NOT IN (select watch_product.p_id from watch_product);";

		PreparedStatement ps9 = conn.prepareStatement(sql9);

		// execute and return a resultSet
		ResultSet resultSet9 = ps9.executeQuery();
		while (resultSet9.next()) {

			String title = resultSet9.getString(1);
			String title2 = resultSet9.getString(2);
			System.out.println(title + title2);
		}
		System.out.println("Actually all the episode have been watched in 2019");

		// Q8
		System.out.println();
		System.out.println("Q8: List the top 5 actors/actresses that appeared in the most movies.");

		String sql10 = "SELECT Name, count( * ) AS count  \r\n"
				+ "FROM crew natural join crew_jobs natural join movie\r\n" + "where job = \"actor/actress\"\r\n"
				+ "GROUP BY cr_id\r\n" + "ORDER BY count DESC  \r\n" + "LIMIT 5;";

		PreparedStatement ps10 = conn.prepareStatement(sql10);

		// execute and return a resultSet
		ResultSet resultSet10 = ps10.executeQuery();
		while (resultSet10.next()) {

			String name = resultSet10.getString(1);
			int count = resultSet10.getInt(2);

			System.out.printf("%5s", "name:" + name + " " + "show time:" + count + "\n");
		}
		// Q9
		System.out.println();
		System.out.println("Q9: List the top 10 actors/actresses that appeared in the most episodes of TV shows");

		String sql11 = "SELECT Name, count( * ) AS count  \r\n"
				+ "FROM crew natural join crew_jobs natural join episode\r\n" + "where job = \"actor/actress\"\r\n"
				+ "GROUP BY cr_id\r\n" + "ORDER BY count DESC  \r\n" + "LIMIT 10;";

		PreparedStatement ps11 = conn.prepareStatement(sql11);

		// execute and return a resultSet
		ResultSet resultSet11 = ps11.executeQuery();
		while (resultSet11.next()) {

			String name = resultSet11.getString(1);
			int count = resultSet11.getInt(2);

			System.out.printf("%10s", "name:" + name + " " + "show time:" + count + "\n");
		}

		// Q10
		System.out.println();
		System.out.println("Q10: What is number of subscription customer and their average payment for 2019 \r\n"
				+ "as well as the number of non-subscription customers and the average spend for 2019 \r\n"
				+ "(rentals and purchases)?\r\n" + "");

		String sql12 = "select count(*) as number \r\n" + "from subscriber;";

		PreparedStatement ps12 = conn.prepareStatement(sql12);

		// execute and return a resultSet
		ResultSet resultSet12 = ps12.executeQuery();
		while (resultSet12.next()) {

			int count = resultSet12.getInt(1);

			System.out.println("The number of subscription customer is " + count);
		}

		String sql13 = "select avg(price) as average_payment\r\n" + "from subscriber;\r\n";

		PreparedStatement ps13 = conn.prepareStatement(sql13);

		// execute and return a resultSet
		ResultSet resultSet13 = ps13.executeQuery();
		while (resultSet13.next()) {

			double avg = resultSet13.getDouble(1);

			System.out.println("Subscription customer average spend is " + avg);
		}

		String sql14 = "select count(c_id) as number \r\n" + "from customers\r\n"
				+ "where c_id not in (select c_id \r\n" + "from subscriber);\r\n";

		PreparedStatement ps14 = conn.prepareStatement(sql14);

		// execute and return a resultSet
		ResultSet resultSet14 = ps14.executeQuery();
		while (resultSet14.next()) {

			int count = resultSet14.getInt(1);

			System.out.println("Number of non-subscription customers " + count);
		}

		String sql15 = "\r\n" + "select avg(rent_price) +(select avg(buy_price)\r\n"
				+ "from purchaser natural join buy_product natural join products\r\n"
				+ ") as  average_spend_for_2019\r\n"
				+ "from purchaser natural join rent_product natural join products;\r\n" + "";

		PreparedStatement ps15 = conn.prepareStatement(sql15);

		// execute and return a resultSet
		ResultSet resultSet15 = ps15.executeQuery();
		while (resultSet15.next()) {

			double avg = resultSet15.getDouble(1);

			System.out.println("Average spend for 2019 is " + avg);
		}

		closeResource(conn, ps);
		closeResource(conn, ps1);
		closeResource(conn, ps2);
		closeResource(conn, ps3);
		closeResource(conn, ps4);
		closeResource(conn, ps5);
		closeResource(conn, ps6);
		closeResource(conn, ps7);
		closeResource(conn, ps8);
		closeResource(conn, ps9);
		closeResource(conn, ps10);
		closeResource(conn, ps11);
		closeResource(conn, ps12);
		closeResource(conn, ps13);
		closeResource(conn, ps14);
		closeResource(conn, ps15);

	}

	public static void closeResource(Connection conn, Statement ps) {
		try {
			// 资源的关闭
			if (ps != null) {
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
