# SSH整合开发

## 1. web.xml配置
web.xml需要配置Spring框架的核心监听器、Struts2框架的核心过滤器和Spring配置文件applicationContext.xml的路径

### （1）Spring框架的核心监听器配置：

```xml
	<!-- Spring框架的核心监听器 -->
  <listener>
	<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
  </listener>
```

### （2）Struts2框架的核心过滤器配置：

```xml
	<!-- Struts2框架的核心过滤器的配置 -->
  <filter>
  	<filter-name>struts</filter-name>
	<filter-class>org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter</filter-class>
  </filter>
  
  <filter-mapping>
  	<filter-name>struts</filter-name>
  	<url-pattern>/*</url-pattern>
  </filter-mapping>
```

### （3）Spring配置文件applicationContext.xml的路径配置

```xml
<!-- 环境参数: Spring配置文件applicationContext.xml的路径 -->
<context-param>
  	<param-name>contextConfigLocation</param-name>
  	<param-value>classpath:applicationContext.xml</param-value>
  </context-param>
```

------

## 2. Spring配置文件applicationContext.xml配置

### （1）对于Spring的配置文件，应该包含所有的Bean，以便Spring能进行依赖注入，并且让Spring代理以实现AOP编程。

常用的Bean配置：
c3p0的连接池；Hibernate的会话工厂sessionFactory 和事务管理器transactionManager；Action类，Service类，Dao类

### （2）另外还需配置开启事务管理和引入外部的属性文件（比如JDBC的属性文件）

### （3）详细配置如下：

① 引入外部属性文件：

```xml
<!-- 引入外部的属性文件 -->
	<context:property-placeholder location="classpath:jdbc.properties" />
```

② c3p0连接池（这里的 ${jdbc.*} 表示引用外部的JDBC属性文件中的属性）:

```xml
<!-- 配置连接池 -->
	<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
		<property name="driverClass" value="${jdbc.driverClass}" />
		<property name="jdbcUrl" value="${jdbc.url}" />
		<property name="user" value="${jdbc.username}" />
		<property name="password" value="${jdbc.password}" />
	</bean>
```

③ 配置Hibernate会话工厂的相关属性

```xml
<!-- 配置Hibernate相关属性 -->
	<bean id="sessionFactory" class="org.springframework.orm.hibernate3.LocalSessionFactoryBean">
		<!-- 注入连接池 -->
		<property name="dataSource" ref="dataSource" />
		<!-- 配置Hibernate的属性 -->
		<property name="hibernateProperties">
			<props>
				<prop key="hibernate.dialect">org.hibernate.dialect.MySQLDialect</prop>
				<prop key="hibernate.show_sql">true</prop>
				<prop key="hibernate.format_sql">true</prop>
				<prop key="hibernate.hbm2ddl.auto">update</prop>
			</props>
		</property>
		<!-- 加载Hibernate中的映射文件 -->
		<property name="mappingResources">
			<list>
				<value>com/ssh/model/Product2.hbm.xml</value>
			</list>
		</property>
	</bean>
```

④ 配置事务管理器

```xml
<!-- 配置事务管理器 -->
	<bean id="transactionManager" class="org.springframework.orm.hibernate3.HibernateTransactionManager">
		<property name="sessionFactory" ref="sessionFactory"/>
	</bean>
```

⑤ 开启注解事务

```xml
<!-- 开启注解事务 -->
	<tx:annotation-driven transaction-manager="transactionManager" />
```

⑥ 配置Action类，Service类，Dao类

```xml
<!-- 配置 Action 的类 -->
	<bean id="productAction" class="com.ssh.action.ProductAction" scope="prototype">
		<property name="productService" ref="productService" />
	</bean>

<!-- 配置 Service 的类 -->
	<bean id="productService" class="com.ssh.service.ProductService">
		<property name="productDao" ref="productDao"/>
	</bean>

<!-- 配置 Dao 的类 -->
	<bean id="productDao" class="com.ssh.dao.ProductDao">
		<property name="sessionFactory" ref="sessionFactory" />
	</bean>
```

------

## 3. Struts配置文件struts.xml配置
Struts配置文件struts.xml主要配置Web层的访问请求所需要调用的类和方法。
其调用的Action类名应该写成Spring配置文件中的BeanId，以便能让Spring代理类和方法的调用，实现AOP编程
其调用的方法名可通过通配符（*）的方式拼接在url地址中
例子如下：

```xml
	<action name="product_*" class="productAction" method="{1}" >
	</action>
```

action中的name属性表示访问的url地址，class是Spring配置文件中Bean的Id为productAction中的类，method表示调用name属性中*处的方法（访问product_save则调用productAction类的save()方法）

配置如下：

```xml
<struts>
	<package name="ssh" extends="struts-default" namespace="/">
		<action name="product_*" class="productAction" method="{1}" >
		</action>
	</package>	
</struts>
```

------

## 4. Spring与Struts整合
Spring和Struts的整合主要关注Service类和Action类

### （1）Action类：
Action类需继承ActionSupport类并实现ModelDriven接口。继承ActionSupport类表明该类是Web层的类，即可被Struts调用。实现ModelDriven接口则是为了在网页端可以通过getModel()方法获取对象obj，并通过obj.set…()方法设置相应的属性（此步通过反射来实现）。当设值完成后，在调用struts中配置的方法（比如login）。

### （2）Service类：
Service类负责处理业务逻辑，注意若Service中存在对数据库进行写操作的逻辑时，需要在类上加上*@Transactional*注解。

------

## 5. Spring与Hibernate整合
Spring与Hibernate整合主要关注和数据库有关的Dao类，Model类和表的配置文件*.hbm.xml。

### （1）Dao类：
Dao类需继承HibernateSupport，以便能获取并调用Hibernate模板HibernateTemplate的方法。

### （2）Model类：
Model类主要是ORM对象关系映射，其中成员变量对应数据表中的列，实现将对象保存到数据库中。

### （3）*.hbm.xml配置文件
hbm.xml文件配置Java类与表的对应关系，以及属性和列的对应关系

------

## 6. jsp页面编写
对于jsp页面中表单的属性名（name）的值，需要和getModel() 方法返回的对象中的成员变量名相同，这样struts才能将值存入model中。

------

Github源码地址：https://github.com/api-jackson/SSH_Start

