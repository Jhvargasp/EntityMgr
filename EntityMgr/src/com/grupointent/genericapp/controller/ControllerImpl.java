package com.grupointent.genericapp.controller;

import com.grupointent.genericapp.dao.ObjectDao;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.SingleTableEntityPersister;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ControllerImpl {
	private final ObjectDao objectDao;
	private final String JSP = "/generic/principal";
	private static Logger log = Logger.getLogger(ControllerImpl.class);

	@Autowired
	public ControllerImpl(ObjectDao objectDao) {
		this.objectDao = objectDao;
	}

	@RequestMapping({ "/list.html" })
	public String list(@RequestParam("fullResults") boolean fullResults, @RequestParam("entity") String entity,
			ModelMap model, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		List<Object> ls = this.objectDao.query("FROM " + entity, fullResults);
		model.addAttribute("objects", ls);
		model.addAttribute("name", entity.toUpperCase());
		model.addAttribute("entityName", entity);
		String[] c = getColumns(entity);
		model.addAttribute("columns", c);

		log.debug(entity + " size>" + ls.size());

		return "/list";
	}

	@RequestMapping({ "/populateCombo.html" })
	public String populateCombo(@RequestParam("query") String sql, ModelMap model, HttpSession session,
			HttpServletRequest request, HttpServletResponse response) {
		List<Object> ls = this.objectDao.querySQL(sql);

		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();

			response.setContentType("text/xml");
			StringBuffer buffer = new StringBuffer("<data>");
			for (Object object : ls) {
				buffer.append("<row>");
				int idx = 0;
				if ((object instanceof Object[])) {
					Object[] arr = (Object[]) object;
					Object[] arrayOfObject1;
					int j = (arrayOfObject1 = arr).length;
					for (int i = 0; i < j; i++) {
						Object object2 = arrayOfObject1[i];
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
		return null;
	}

	@RequestMapping({ "/search.html" })
	public String search(@RequestParam("fullResults") boolean fullResults, @RequestParam("entity") String entity,
			@RequestParam("filtro") String field, @RequestParam("fltrValue") String value, ModelMap model,
			HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		String c = getColumnName(entity, field);

		int tipo = getTypeColumn(entity, field);
		String SQL = " WHERE " + c;
		switch (tipo) {
		case 1:
			SQL = SQL + " LIKE '%" + value + "%'";
			break;
		default:
			SQL = SQL + " = " + value;
		}
		if (value.trim().length() == 0) {
			SQL = "";
		}
		try {
			List<Object> ls = this.objectDao.query("FROM " + entity + SQL, fullResults);
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

		return "/list";
	}

	private String getColumnName(String entity, String field) {
		ClassMetadata entityMetaData = this.objectDao.getSessionFactory().getClassMetadata(entity);
		SingleTableEntityPersister stable = (SingleTableEntityPersister) entityMetaData;

		String ret = "";
		ret = stable.getPropertyColumnNames(field)[0];
		return ret;
	}

	private String[] getColumns(String entity) {
		ClassMetadata entityMetaData = this.objectDao.getSessionFactory().getClassMetadata(entity);
		SingleTableEntityPersister stable = (SingleTableEntityPersister) entityMetaData;

		String[] names = new String[entityMetaData.getPropertyNames().length + 1];
		String[] cnames = new String[entityMetaData.getPropertyNames().length + 1];
		names[0] = entityMetaData.getIdentifierPropertyName();
		cnames[0] = stable.getPropertyColumnNames(names[0])[0];
		for (int i = 0; i < entityMetaData.getPropertyNames().length; i++) {
			names[(i + 1)] = entityMetaData.getPropertyNames()[i];
		}
		return names;
	}

	private int getTypeColumn(String entity, String field) {
		ClassMetadata entityMetaData = this.objectDao.getSessionFactory().getClassMetadata(entity);
		Type t = entityMetaData.getPropertyType(field);
		int retorno = 0;
		if (t.getName().equalsIgnoreCase("string")) {
			retorno = 1;
		}
		return retorno;
	}

	@RequestMapping({ "/get.html" })
	public String get(@RequestParam("id") String id, @RequestParam("entity") String entity, ModelMap model,
			HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		if (id.length() > 0) {
			model.addAttribute("values", this.objectDao.get(id, entity));
			model.addAttribute("id", id);
		}
		ClassMetadata entityMetaData = this.objectDao.getSessionFactory().getClassMetadata(entity);
		String[] names = getColumns(entity);

		HashMap propType = new HashMap();
		String[] arrayOfString1;
		int j = (arrayOfString1 = names).length;
		for (int i = 0; i < j; i++) {
			String tipo = arrayOfString1[i];
			System.out.println(entityMetaData.getIdentifierPropertyName() + " " + entityMetaData.getIdentifierType());
			propType.put(tipo, entityMetaData.getPropertyType(tipo).getName());
		}
		model.addAttribute("properties", names);
		model.addAttribute("propTypes", propType);
		model.addAttribute("entity", entity);

		return "/entity";
	}

	@RequestMapping({ "/del.html" })
	public String delete(@RequestParam("fullResults") boolean fullResults, @RequestParam("id") String id,
			@RequestParam("entity") String entity, ModelMap model, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		this.objectDao.delete(this.objectDao.get(id, entity));
		return list(fullResults, entity, model, session, request, response);
	}

	@RequestMapping({ "/update.html" })
	public String update(@RequestParam("fullResults") boolean fullResults, @RequestParam("id") String id,
			@RequestParam("entity") String entity, ModelMap model, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		HashMap values = new HashMap();
		if (id.trim().length() > 0) {
			values = (HashMap) this.objectDao.get(id, entity);
		} else {
			values.put("$type$", entity);
		}
		String[] names = getColumns(entity);

		ClassMetadata entityMetaData = this.objectDao.getSessionFactory().getClassMetadata(entity);
		HashMap propType = new HashMap();
		String[] arrayOfString1;
		int j = (arrayOfString1 = names).length;
		for (int i = 0; i < j; i++) {
			String tipo = arrayOfString1[i];
			propType.put(tipo, entityMetaData.getPropertyType(tipo).getName());
		}
		j = (arrayOfString1 = names).length;
		for (int i = 0; i < j; i++) {
			String var = arrayOfString1[i];
			Object value = request.getParameter(var);
			if ("integer".equalsIgnoreCase(propType.get(var).toString())) {
				try {
					value = Integer.valueOf(Integer.parseInt(value.toString()));
				} catch (Exception localException) {
				}
			} else if ("date".equalsIgnoreCase(propType.get(var).toString())) {
				try {
					if (value.toString().length() > 0) {
						value = new SimpleDateFormat("dd-MM-yyyy").parse(value.toString());
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			log.debug(var + ">>" + value + ">>" + values);
			if ((var != null) && (value != null) && (value.toString().length() > 0)) {
				values.put(var, value);
			}
		}
		values.put("id", request
				.getParameter(this.objectDao.getSessionFactory().getClassMetadata(entity).getIdentifierPropertyName()));
		this.objectDao.saveOrUpdate(values);

		return list(fullResults, entity, model, session, request, response);
	}
}
