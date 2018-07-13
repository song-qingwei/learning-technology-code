package com.example.model;

/**
 * @author SongQingWei
 * @date 2018年6月26日 上午9:58:35
 */
public class Authority {

	private Integer id;

    private String name;

    private String url;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "Authority [id=" + id + ", name=" + name + ", url=" + url + "]";
	}
}
