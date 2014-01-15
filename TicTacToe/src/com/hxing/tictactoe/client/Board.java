package com.hxing.tictactoe.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;


public class Board extends Composite {
	
	
	Grid grid;
	Cell resetCell;
	Cell[][]cells;
	Button reset;
	char whoseToken;
	Label whoseTurn;
	VerticalPanel main;
	HorizontalPanel holder;
	PopupPanel pop;
	Button restart;
	
	
	private void init(){
		resetCell=null;
		grid=new Grid(3,3);
		whoseToken='X';
		whoseTurn=new Label();
		holder=new HorizontalPanel();
		main=new VerticalPanel();
		reset=new Button("Undo");
		cells=new Cell[3][3];
		pop=new PopupPanel();
		restart=new Button("Restart the game? Click for yes.");
		
		
		
		whoseTurn.setText("X's Turn");
		grid.setBorderWidth(5);
		grid.setSize("180px", "180px");
		for(int i=0; i<grid.getRowCount(); i++){
			for(int j=0; j<grid.getColumnCount(); j++){	
				Cell c = new Cell();
				cells[i][j]=c;
				grid.setWidget(i, j, c);
			}
		}
		pop.add(restart);
		reset.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				resetCell.reset();
			}	
		});	
		restart.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				for(int i=0; i<cells.length; i++){
					for(Cell c : cells[i]){
						c.setToken(' ');
						c.context.clearRect(0, 0, c.c.getCanvasElement().getWidth(), c.c.getCanvasElement().getHeight());
					}
				}
				whoseToken='X';
				whoseTurn.setText(whoseToken + "'s Turn");
				pop.hide();
			}
			
		});
		whoseTurn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		whoseTurn.setWordWrap(true);
		
		holder.add(whoseTurn);
		holder.add(reset);
		holder.setBorderWidth(5);
		holder.setWidth("200px");
		holder.setCellHorizontalAlignment(reset, HasHorizontalAlignment.ALIGN_JUSTIFY);
		holder.setCellHorizontalAlignment(whoseTurn, HasHorizontalAlignment.ALIGN_JUSTIFY);
		
		main.setSize("250px", "250px");
		main.add(holder);
		main.add(grid);
		main.setCellHorizontalAlignment(holder, HasHorizontalAlignment.ALIGN_CENTER);
		main.setCellVerticalAlignment(grid, HasVerticalAlignment.ALIGN_MIDDLE);
		main.setCellHorizontalAlignment(grid, HasHorizontalAlignment.ALIGN_CENTER);
		main.setStyleName("center");
		
		initWidget(main);
	}
	
	public Board(){
		init();
	}
	
	public boolean isFull(){
		for(int i=0; i<cells.length; i++){
			for(int j=0; j<cells.length; j++){
				if(cells[i][j].token==' '){
					return false;
				}
			}
		}
		return true;
	}
	public boolean isWon(char token){
		for(int i=0; i<3; i++){
			if(cells[i][0].token==token && cells[i][1].token==token && cells[i][2].token==token){
				return true;
			}
		}
		for(int i=0; i<3; i++){
			if(cells[0][i].token==token && cells[1][i].token==token && cells[2][i].token==token){
				return true;
			}
		}
		if(cells[0][0].token==token && cells[1][1].token==token && cells[2][2].token==token){
			return true;
		}
		
		if(cells[0][2].token==token && cells[1][1].token==token && cells[2][0].token==token){
			return true;
		}
		return false;
	}
	
	public class Cell extends Composite{
		
		Canvas c;
		Context2d context;
		char token;
		
		public Cell(){
			init();
		}
		
		private void setToken(char token){
			this.token=token;
			
		}
		
		private void init(){
			c=Canvas.createIfSupported();
			c.setSize("60px", "60px");
			c.setCoordinateSpaceWidth(60);
			c.setCoordinateSpaceHeight(60);
			context=c.getContext2d();
			token=' ';
			c.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					if(!isFull() || !isWon(whoseToken)){
						setReset();
						setToken(whoseToken);
						if(token=='X'){
							context.beginPath();
							context.moveTo(5,5);
							context.lineTo(55,55);
							context.stroke();
							context.closePath();
							context.beginPath();
							context.moveTo(55, 5);
							context.lineTo(5, 55);
							context.stroke();
							context.closePath();
						}
						else{
							context.beginPath();
							context.arc(30, 30, 25, 0, 2*Math.PI);
							context.stroke();
							context.closePath();
						}
						
					}
					if(isWon(whoseToken)){
						whoseTurn.setText(whoseToken+" had won! Game over");
						pop.showRelativeTo(grid);
						
					}
					else if(isFull()){
						whoseTurn.setText("Draw! Game over");
						pop.showRelativeTo(grid);
					}
					else{
						whoseToken=(whoseToken=='X')? 'O' : 'X';
						whoseTurn.setText(whoseToken+"'s Turn");
					}			
				}
				
			});
			initWidget(c);
		}

		
		private void reset(){
			context.clearRect(0, 0, c.getCanvasElement().getWidth(), c.getCanvasElement().getHeight());
			setToken(' ');
			whoseToken=(whoseToken=='X')? 'O' : 'X';
			whoseTurn.setText(whoseToken+"'s Turn");
		}
		
		private void setReset(){
			resetCell=this;
		}
		
	}

}


