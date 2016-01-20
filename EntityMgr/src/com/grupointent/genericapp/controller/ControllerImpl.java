package com.grupointent.genericapp.controller;

import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.BasicEntityPropertyMapping;
import org.hibernate.persister.entity.SingleTableEntityPersister;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.grupointent.genericapp.dao.ObjectDao;

/**
 * @author Jvargas
 * 
 */
@Controller
public class ControllerImpl {

	private final ObjectDao objectDao;

	private final String JSP = "/generic/principal";

	private static Logger log = Logger.getLogger(ControllerImpl.class);

	@Autowired
	public ControllerImpl(ObjectDao objectDao) {
		this.objectDao = objectDao;

	}

	@RequestMapping("/list.html")
	public String list(@RequestParam("entity") String entity, ModelMap model,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		// request.getPathTranslated()
		List<Object> ls = objectDao.query("FROM " + entity);
		model.addAttribute("objects", ls);
		model.addAttribute("name", entity.toUpperCase());
		model.addAttribute("entityName", entity);
		String[] c = getColumns(entity);
		model.addAttribute("columns", c);
		// model.addAttribute("fcolumns", c[1]);
		log.debug(entity + " size>" + ls.size());
		// {email=asdf, login=01, $type$=Employees, displayname=vargas niño}
		return "/list";

	}

	@RequestMapping("/populateCombo.html")
	public String populateCombo(@RequestParam("query") String sql,
			ModelMap model, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		// request.getPathTranslated()
		List<Object> ls = objectDao.querySQL(sql);
		// model.addAttribute("objects", ls);

		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();

			response.setContentType("text/xml");
			StringBuffer buffer = new StringBuffer("<data>");

			for (Object object : ls) {
				buffer.append("<row>");
				int idx = 0;
				if (object instanceof Object[]) {
					Object[] arr = (Object[]) object;
					for (Object object2 : arr) {
						buffer.append("<data" + idx + ">");
						buffer.append(object2);
						buffer.append("</data" + idx + ">");
						idx++;
					}
				} else {
					buffer.append("<data" + idx + ">");
					buffer.append(object);
					buffer.append("</data" + idx + ">");

				}
				buffer.append("</row>");
			}

			buffer.append("</data>");
			out.println(buffer.toString());
			out.flush();
		} catch (Exception x) {
			if (out != null) {
				out.println("");
				out.flush();
			}
		}
		// return builder.toString();
		return null;

	}

	@RequestMapping("/search.html")
	public String search(@RequestParam("entity") String entity,
			@RequestParam("filtro") String field,
			@RequestParam("fltrValue") String value, ModelMap model,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {

		String c = getColumnName(entity, field);

		int tipo = getTypeColumn(entity, field);
		String SQL = " WHERE " + c;
		switch (tipo) {

		case 1:
			SQL += " LIKE '%" + value + "%'";
			break;
		default:
			SQL += " = " + value;
			break;

		}
		if (value.trim().length() == 0) {
			SQL = "";
		}
		try {
			List<Object> ls = objectDao.query("FROM " + entity + SQL);
			log.debug(entity + " size>" + ls.size());

			model.addAttribute("objects", ls);
		} catch (Exception e) {
			log.debug(e);
		}
		model.addAttribute("name", entity.toUpperCase());
		model.addAttribute("entityName", entity);
		model.addAttribute("columns", getColumns(entity));

		model.addAttribute("filtro", field);
		model.addAttribute("fltrValue", value);
		// {email=asdf, login=01, $type$=Employees, displayname=vargas niño}
		return "/list";

	}

	private String getColumnName(String entity, String field) {
		ClassMetadata entityMetaData = objectDao.getSessionFactory()
				.getClassMetadata(entity);
		SingleTableEntityPersister stable = (SingleTableEntityPersister) entityMetaData;

		String ret = "";
		ret = stable.getPropertyColumnNames(field)[0];
		return ret;

	}

	private String[] getColumns(String entity) {
		ClassMetadata entityMetaData = objectDao.getSessionFactory()
				.getClassMetadata(entity);
		SingleTableEntityPersister stable = (SingleTableEntityPersister) entityMetaData;
		// stable.getPropertyColumnNames(i);
		String[] names = new String[entityMetaData.getPropertyNames().length + 1];
		String[] cnames = new String[entityMetaData.getPropertyNames().length + 1];
		names[0] = entityMetaData.getIdentifierPropertyName();
		cnames[0] = stable.getPropertyColumnNames(names[0])[0];
		// entityMetaData.
		for (int i = 0; i < entityMetaData.getPropertyNames().length; i++) {
			// entityMetaData.
			names[i + 1] = entityMetaData.getPropertyNames()[i];
			// cnames[i + 1] = stable.getPropertyColumnNames(entityMetaData
			// .getPropertyNames()[i])[0];
		}
		// String[][] ret = new String[2][];
		// ret[0] = names;
		// ret[1] = cnames;
		return names;

	}

	private int getTypeColumn(String entity, String field) {
		ClassMetadata entityMetaData = objectDao.getSessionFactory()
				.getClassMetadata(entity);
		Type t = entityMetaData.getPropertyType(field);
		int retorno = 0;
		if (t.getName().equalsIgnoreCase("string")) {
			retorno = 1;
		}

		return retorno;

	}

	@RequestMapping("/get.html")
	public String get(@RequestParam("id") String id,
			@RequestParam("entity") String entity, ModelMap model,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		if (id.length() > 0) {
			model.addAttribute("values", objectDao.get(id, entity));
			model.addAttribute("id", id);

		}
		// $type$=Employees
		// validations
		ClassMetadata entityMetaData = objectDao.getSessionFactory()
				.getClassMetadata(entity);
		String[] names = getColumns(entity);
		// String[] names = cnames[0];
		HashMap propType = new HashMap();
		for (String tipo : names) {
			System.out.println(entityMetaData.getIdentifierPropertyName() + " "
					+ entityMetaData.getIdentifierType());
			propType.put(tipo, entityMetaData.getPropertyType(tipo).getName());

		}
		model.addAttribute("properties", names);
		model.addAttribute("propTypes", propType);
		model.addAttribute("entity", entity);

		return "/entity";
	}

	@RequestMapping("/del.html")
	public String delete(@RequestParam("id") String id,
			@RequestParam("entity") String entity, ModelMap model,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		objectDao.delete(objectDao.get(id, entity));
		return list(entity, model, session, request, response);

	}

	@RequestMapping("/update.html")
	public String update(@RequestParam("id") String id,
			@RequestParam("entity") String entity, ModelMap model,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		HashMap values = new HashMap();
		if (id.trim().length() > 0) {
			values = (HashMap) objectDao.get(id, entity);

		} else {
			values.put("$type$", entity);
		}
		String[] names = getColumns(entity);
		// String[] names = cnames[0];

		ClassMetadata entityMetaData = objectDao.getSessionFactory()
				.getClassMetadata(entity);
		HashMap propType = new HashMap();
		for (String tipo : names) {
			propType.put(tipo, entityMetaData.getPropertyType(tipo).getName());

		}
		for (String var : names) {
			Object value = request.getParameter(var);
			if ("integer".equalsIgnoreCase(propType.get(var).toString())) {
				try {
					value = Integer.parseInt(value.toString());
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else if ("date".equalsIgnoreCase(propType.get(var).toString())) {
				try {
					if (value.toString().length() > 0)
						value = new SimpleDateFormat("dd-MM-yyyy").parse(value
								.toString());
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			log.debug(var + ">>" + value + ">>" + values);
			if (var != null && value != null && value.toString().length() > 0)
				values.put(var, value);
		}

		values.put("id", request.getParameter(objectDao.getSessionFactory()
				.getClassMetadata(entity).getIdentifierPropertyName()));
		objectDao.saveOrUpdate(values);

		return list(entity, model, session, request, response);

	}
}
