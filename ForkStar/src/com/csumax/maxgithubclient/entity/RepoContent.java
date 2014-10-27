package com.csumax.maxgithubclient.entity;

import org.eclipse.egit.github.core.TreeEntry;

public class RepoContent implements Comparable<RepoContent> {

	private TreeEntry treeEntry;

	public RepoContent(TreeEntry treeEntry) {
		super();
		this.treeEntry = treeEntry;
	}

	@Override
	public int compareTo(RepoContent another) {
		// TODO Auto-generated method stub
		if (this.treeEntry.getType() != null) {
			return another.getTreeEntry().getType().toLowerCase()
					.compareTo(this.treeEntry.getType().toLowerCase());
		} else {
			throw new IllegalArgumentException();
		}

	}

	public TreeEntry getTreeEntry() {
		return treeEntry;
	}

	public void setTreeEntry(TreeEntry treeEntry) {
		this.treeEntry = treeEntry;
	}

}
