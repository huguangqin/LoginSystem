package loginsystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class LoginSystem {

	public static void main(String[] args) throws IOException {
		// 定义一个集合,用来存储用户信息,以后所有操作都是以该集合中的对象为基本单位
		ArrayList<UserInformation> list = new ArrayList<>();
		// 程序启动需要先读取存在本地的文件
		reload(list);
		// 客户界面仅保持三个选项:1.注册,2.登陆,3.退出
		printWelcome();
		Scanner sc = new Scanner(System.in);
		// 使用死循环确保程序可以进行完整的功能操作而不退出
		while (true) {
			String choose = sc.next();
			switch (choose) {
			// 注册功能
			case "1":
				regist(list);
				System.out.println("请继续选择功能:");
				break;
			// 登陆功能
			case "2":
				log(list);
				System.out.println("请继续选择功能:");
				break;
			// 退出功能
			case "3":
				save(list);
				System.out.println("系统退出");
				System.exit(0); // 0表示正常退出虚拟机
			default:
				System.out.println("选择有误,请重新选择");
			}
		}
	}

	/*
	 * 显示主界面方法 名称:printWelcome 返回值:void 参数:无
	 */
	public static void printWelcome() {
		// 调用StringBuilder对象连接字符串,提高效率.避免使用每次打印一行或者字符串的相加运算.
		StringBuilder sb = new StringBuilder();
		sb.append("=======欢迎注册=============\n");
		sb.append("1.注册\n");
		sb.append("2.登陆\n");
		sb.append("3.退出\n");
		sb.append("请选择:");
		System.out.println(sb);
	}

	/*
	 * 注册方法 名称:regist() 返回值:void 参数:ArrayList<UserInformation> list
	 */
	public static void regist(ArrayList<UserInformation> list) {
		Scanner sc = new Scanner(System.in);
		// 由于本方法内name变量一直使用,故在循环外定义
		String name;
		while (true) {
			// 提示输入用户名
			System.out.println("请输入用户名:");
			name = sc.next();
			// 判断用户名是否重复,重复则要求重新输入
			boolean isExist = false; // 通过设置标记,将两层逻辑分开,减少逻辑嵌套
			for (int j = 0; j < list.size(); j++) {
				if (name.equals(list.get(j).getName())) {
					isExist = true;
					break;
				}
			}// for(j)
				// 如果标记为true,说明集合内已存在该用户名,需重新运行一遍以上程序;如果标记为fasle,直接退出while(true)
			if (isExist) {
				System.out.println("用户名已占用,请重新输入");
			} else {
				break;
			}
		}// while(true)
			// 提示输入密码
		System.out.println("请输入密码:");
		String passWord = sc.next();
		// 确认 密码
		boolean flag = false;
		while (flag) {
			System.out.println("请再输入一次:");
			String passWordEnsure = sc.next();
			// 前后密码输入一致,则通过,否则将flag标记设为true,继续执行死循环
			if (passWord.equals(passWordEnsure)) {
				break;
			} else {
				flag = true;
			}
		}// while(flag)
			// 以上基于字符串进行验证,待验证完成后,构建UserInformation对象
		UserInformation uf = new UserInformation(name, passWord);
		// 加入集合中
		list.add(uf);
		// 提示用户"注册成功"
		System.out.println("注册成功!");
		// 调用save()方法,将该用户信息(集合)写入本地文件
		save(list);
	}

	/*
	 * 登陆方法:名称:log 返回值:void 参数:ArrayList<UserInformation> list
	 */
	public static void log(ArrayList<UserInformation> list) {
		Scanner sc = new Scanner(System.in);
		// 用户名密码若不匹配,最多有3次机会,因此设置3次循环(对于次数确定的循环,需用for)
		for (int j = 3; j >= 0; j--) {
			// 提示输入用户名
			System.out.println("请输入用户名:");
			String name = sc.next();
			// 提示输入密码
			System.out.println("请输入密码:");
			String passWord = sc.next();
			/*
			 * 搜索信息库中是否有该用户信息,如果存在用户名且与密码匹配,则将标记设为true,然后通过下周的if-else判断提示"欢迎回来";否则
			 * ,一直查找完全部集合,不能加else语句.若加上 else语句,则第一次找不到就会进入下一步的if-else判断
			 */
			boolean flag = false;
			for (int i = 0; i < list.size(); i++) { // 犯错:必须一直查找,不能加else语句,完全遍历集合,找到匹配的对象,然后将flag标为true;
				if (name.equals(list.get(i).getName())
						&& passWord.equals(list.get(i).getPassWord())) {
					flag = true;
				}
			}// for(i)
				// 如果标记为true,则提示"欢迎回来",否则继续判断是否还有3次机会,并在最后一次机会,提示没有机会了,并自动退出系统
			if (flag) {
				System.out.println("欢迎回来!");
				return;
			} else {
				if (j == 0) {
					System.out.println("你没有机会了,系统退出");
					System.exit(0);
				} else {
					System.out.println("输入有误,请重新输入!,你还有" + j + "次机会");
				}
			}
		}// for(j)

	}

	/*
	 * 读取方法 名称:reload 返回值:ArrayList<UserInformation>
	 * 参数:ArrayList<UserInformation> list
	 */
	public static ArrayList<UserInformation> reload(
			ArrayList<UserInformation> list) {
		// 按行读取数据时,需构造BufferedReader对象,此处需注意Exception,及关闭流
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader("ufo.txt"));
			String line;
			while ((line = br.readLine()) != null) {
				String[] str = line.split(",");
				UserInformation uf = new UserInformation(str[0], str[1]);
				list.add(uf);
			}
		} catch (IOException e) {
			System.out.println("文件读取出错,请尝试在程序根目录下建立ufo.txt");
			e.printStackTrace();
		} finally {
			// 关闭流
			try {
				br.close();
			} catch (IOException e) {
				System.out.println("关闭读取流出错");
				e.printStackTrace();
			}
		}
		// 读取后返回集合
		return list;
	}

	/*
	 * 写入方法 名称:save 返回值:void 参数:ArrayList<UserInformation>
	 */
	public static void save(ArrayList<UserInformation> list) {
		BufferedWriter bw = null;
		// 写入本地文件时,需构造BufferedWriter对象,并注意Exception,并关闭流
		try {
			bw = new BufferedWriter(new FileWriter("ufo.txt"));
			for (int i = 0; i < list.size(); i++) {
				StringBuilder sb = new StringBuilder();
				sb.append(list.get(i).getName()).append(",")
						.append(list.get(i).getPassWord());
				bw.write(sb.toString());
				bw.newLine();
				bw.flush();// 可不写
			}
		} catch (IOException e) {
			System.out.println("文件写入出错!");
			e.printStackTrace();
		} finally {
			// finally中进行关闭流,并注意捕获异常
			try {
				bw.close();
			} catch (IOException e) {
				System.out.println("关闭写入流出错");
				e.printStackTrace();
			}
		}
	}

}
