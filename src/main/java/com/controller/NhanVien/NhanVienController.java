package com.controller.NhanVien;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.model.DAO.ChucVu.ChucVuJDBC;
import com.model.DAO.NhanVien.NhanVienJDBC;
import com.model.*;

@Controller
@RequestMapping(value = "/nhan-vien")
public class NhanVienController {
	private ApplicationContext context;

	@ModelAttribute("themNhanVienBean")
	public ThemNhanVienBean themNhanVienBean() {
		return new ThemNhanVienBean();
	}

	@ModelAttribute("suaNhanVienBean")
	public SuaNhanVienBean suaNhanVienBean() {
		return new SuaNhanVienBean();
	}

	@ModelAttribute("themChucVuBean")
	public ThemChucVuBean themChucVuBean() {
		return new ThemChucVuBean();
	}

	@RequestMapping(method = RequestMethod.GET)
	public String quanLyNhanVien(ModelMap model, HttpServletRequest request) {
		context = new ClassPathXmlApplicationContext("Beans.xml");
		NhanVienJDBC nhanVienJDBC = (NhanVienJDBC) context.getBean("nhanVienJDBC");
		ChucVuJDBC chucVuJDBC = (ChucVuJDBC) context.getBean("chucVuJDBC");
		List<NhanVien> nVList = nhanVienJDBC.getAllActive();
		List<ChucVu> cVList = chucVuJDBC.getAll();
		model.addAttribute("nVList", nVList);
		model.addAttribute("cVList", cVList);
		return "quanlynhanvien";
	}

	@RequestMapping(value = "/ngung-hoat-dong", method = RequestMethod.GET)
	public String ngungHoatDong(ModelMap model, HttpServletRequest request) {
		context = new ClassPathXmlApplicationContext("Beans.xml");
		NhanVienJDBC nhanVienJDBC = (NhanVienJDBC) context.getBean("nhanVienJDBC");
		List<NhanVien> nVList = nhanVienJDBC.getAllDeActive();
		model.addAttribute("nVList", nVList);
		return "nhanvienngunghoatdong";
	}

	@RequestMapping(value = "/ngung-hoat-dong/{nvID}", method = RequestMethod.GET)
	public String ngungHoatDongById(@PathVariable String nvID, ModelMap model, HttpServletRequest request,
			RedirectAttributes redirectAttrs) {
		context = new ClassPathXmlApplicationContext("Beans.xml");
		NhanVienJDBC nhanVienJDBC = (NhanVienJDBC) context.getBean("nhanVienJDBC");
		int xoa = nhanVienJDBC.deactivateById(nvID);
		NhanVien nv = nhanVienJDBC.getNVByMaNV(nvID);
		if (xoa == 1) {
			redirectAttrs.addFlashAttribute("success",
					"Ngưng hoạt động nhân viên " + nv.getTenNhanVien() + " thành công");
		} else {
			redirectAttrs.addFlashAttribute("error", "Ngưng hoạt động nhân viên thất bại");
		}
		return "redirect:/nhan-vien";
	}

	@RequestMapping(value = "/huy", method = RequestMethod.GET)
	public String huy(RedirectAttributes redirectAttrs) {
		redirectAttrs.addFlashAttribute("error", "Hủy thành công");
		return "redirect:/nhan-vien";
	}

	@RequestMapping(value = "/tai-hoat-dong/{nvID}", method = RequestMethod.GET)
	public String taiHoatDongById(@PathVariable String nvID, ModelMap model, HttpServletRequest request,
			RedirectAttributes redirectAttrs) {
		context = new ClassPathXmlApplicationContext("Beans.xml");
		NhanVienJDBC nhanVienJDBC = (NhanVienJDBC) context.getBean("nhanVienJDBC");
		int xoa = nhanVienJDBC.activeById(nvID);
		NhanVien nv = nhanVienJDBC.getNVByMaNV(nvID);
		if (xoa == 1) {
			redirectAttrs.addFlashAttribute("success",
					"Tái hoạt động nhân viên " + nv.getTenNhanVien() + " thành công");
		} else {
			redirectAttrs.addFlashAttribute("error", "Tái hoạt động nhân viên thất bại");
		}
		return "redirect:/nhan-vien/ngung-hoat-dong";
	}

	@RequestMapping(value = "/chuc-vu", method = RequestMethod.GET)
	public String themChucVu(ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttrs) {
		context = new ClassPathXmlApplicationContext("Beans.xml");
		ChucVuJDBC chucVuJDBC = (ChucVuJDBC) context.getBean("chucVuJDBC");
		List<ChucVu> listCV = chucVuJDBC.getAll();
		model.addAttribute("listCV", listCV);
		return "themchucvu";
	}

	@RequestMapping(value = "/chuc-vu", method = RequestMethod.POST)
	public String themChucVuProcess(@ModelAttribute("SpringWeb") ThemChucVuBean themChucVuBean, ModelMap model,
			HttpServletRequest request, RedirectAttributes redirectAttrs) {
		context = new ClassPathXmlApplicationContext("Beans.xml");
		ChucVuJDBC chucVuJDBC = (ChucVuJDBC) context.getBean("chucVuJDBC");
		ChucVu cv = (ChucVu) chucVuJDBC.getLast();
		String tmp = cv.getMaChucVu().substring(2).trim();
		int i = Integer.parseInt(tmp) + 1;
		if (i < 10)
			tmp = "CV000" + i;
		else if (i < 100)
			tmp = "CV00" + i;
		else if (i < 1000)
			tmp = "CV0" + i;
		else
			tmp = "CV" + i;
		int them = chucVuJDBC.add(new ChucVu(tmp, themChucVuBean.getTenChucVu()));
		if (them == 1) {
			redirectAttrs.addFlashAttribute("success", "Thêm chức vụ thành công");
		} else {
			redirectAttrs.addFlashAttribute("error", "Thêm chức vụ thất bại");
		}
		return "redirect:/nhan-vien/chuc-vu";
	}

	@RequestMapping(value = "/them-nhan-vien", method = RequestMethod.GET)
	public String themNhanVien(ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttrs) {
		context = new ClassPathXmlApplicationContext("Beans.xml");
		ChucVuJDBC chucVuJDBC = (ChucVuJDBC) context.getBean("chucVuJDBC");
		List<ChucVu> listCV = chucVuJDBC.getAll();
		model.addAttribute("listCV", listCV);
		return "themnhanvien";
	}

	@RequestMapping(value = "/them-nhan-vien", method = RequestMethod.POST)
	public String themNhanVienProcess(@ModelAttribute("SpringWeb") ThemNhanVienBean themNhanVienBean, ModelMap model,
			HttpServletRequest request, RedirectAttributes redirectAttrs) {
		context = new ClassPathXmlApplicationContext("Beans.xml");
		NhanVienJDBC nhanVienJDBC = (NhanVienJDBC) context.getBean("nhanVienJDBC");
		NhanVien nV = (NhanVien) nhanVienJDBC.getLast();
		String tmp = nV.getMaNhanVien().substring(2).trim();
		int i = Integer.parseInt(tmp) + 1;
		if (i < 10)
			tmp = "NV000" + i;
		else if (i < 100)
			tmp = "NV00" + i;
		else if (i < 1000)
			tmp = "NV0" + i;
		else
			tmp = "NV" + i;
		int them = nhanVienJDBC.add(new NhanVien(tmp, themNhanVienBean.getTenNhanVien(), themNhanVienBean.isGioiTinh(),
				themNhanVienBean.getNgaySinh(), themNhanVienBean.getDiaChi(), themNhanVienBean.getSoDienThoai(),
				themNhanVienBean.getMaChucVu()));
		if (them == 1) {
			redirectAttrs.addFlashAttribute("success",
					"Thêm nhân viên " + themNhanVienBean.getTenNhanVien() + " thành công");
		} else {
			redirectAttrs.addFlashAttribute("error", "Thêm nhân viên thất bại");
		}
		return "redirect:/nhan-vien";
	}

	@RequestMapping(value = "/sua-nhan-vien/{nvID}", method = RequestMethod.GET)
	public String suaNhanVien(@PathVariable String nvID, ModelMap model, HttpServletRequest request,
			RedirectAttributes redirectAttrs) {
		context = new ClassPathXmlApplicationContext("Beans.xml");
		NhanVienJDBC nhanVienJDBC = (NhanVienJDBC) context.getBean("nhanVienJDBC");
		ChucVuJDBC chucVuJDBC = (ChucVuJDBC) context.getBean("chucVuJDBC");
		List<ChucVu> listCV = chucVuJDBC.getAll();
		NhanVien nV = nhanVienJDBC.getNVByMaNV(nvID);
		model.addAttribute("listCV", listCV);
		model.addAttribute("nV", nV);
		return "suanhanvien";
	}

	@RequestMapping(value = "/sua-nhan-vien/{nvID}", method = RequestMethod.POST)
	public String suaNhanVienProcess(@ModelAttribute("SpringWeb") SuaNhanVienBean suaNhanVienBean,
			@PathVariable String nvID, ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttrs) {
		context = new ClassPathXmlApplicationContext("Beans.xml");
		NhanVienJDBC nhanVienJDBC = (NhanVienJDBC) context.getBean("nhanVienJDBC");
		int sua = nhanVienJDBC.sua(new NhanVien(nvID, suaNhanVienBean.getTenNhanVien(), suaNhanVienBean.isGioiTinh(),
				suaNhanVienBean.getNgaySinh(), suaNhanVienBean.getDiaChi(), suaNhanVienBean.getSoDienThoai(),
				suaNhanVienBean.getMaChucVu()));
		if (sua == 1) {
			redirectAttrs.addFlashAttribute("success", "Sửa nhân viên thành công");
		} else {
			redirectAttrs.addFlashAttribute("error", "Sửa nhân viên thất bại");
		}
		return "redirect:/nhan-vien";
	}
}
