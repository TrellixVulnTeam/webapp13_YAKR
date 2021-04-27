import { Component, OnInit } from '@angular/core';
import { PortfolioitemService } from '../../services/portfolioitem.service';
import { LoginService } from '../../services/login.service';

import { Portfolioitem } from '../../models/portfolioitem.model';

@Component({
  selector: 'app-settings-edit-account-portfolioitems',
  templateUrl: './settings-edit-account-portfolioitems.component.html',
  styleUrls: ['./settings-edit-account-portfolioitems.component.css']
})
export class SettingsEditAccountPortfolioitemsComponent implements OnInit {

  portfolioItems: Portfolioitem[] = []
  user:string = this.loginService.currentUser()["id"]
  page: number = 0
  totalElements: number = 0
  actualElements: number = 0
  

  constructor(private portfolioitemService: PortfolioitemService, private loginService: LoginService) { }

  ngOnInit(): void {
    this.getPortfolioItems(this.page)
    this.setTotalPortfolioItems()
    this.page++
  }

  loadMore(): void {
    this.getPortfolioItems(this.page) 
    this.page++
  }
  
  create(previewImg, image1, image2, image3, name: string, category: string, client: string, date: string, url: string, description: string) {
    this.portfolioitemService.post({"userId":"id",name,description,category,client,url,date}).subscribe(
      item => this.portfolioItems.push(item),
      error => console.log("Error")
    )
  }

  private getPortfolioItems(page: number) {
    this.portfolioitemService.getPortfolioItems(this.user, page).subscribe(
      portfolioItems => {
        portfolioItems.map(item => this.portfolioItems.push(new Portfolioitem(item)))
        this.actualElements += portfolioItems.length
      },
      error => console.log("error")
    )
  }


  private setTotalPortfolioItems() {
    this.portfolioitemService.getTotalElements("/api/portfolioItems/users/"+"id"+"?page=" + this.page).subscribe(
      totalElements => this.totalElements = totalElements,
      error => console.log("error getting total elements")
    )
  }
}
