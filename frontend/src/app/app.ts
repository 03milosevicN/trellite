import { Component  } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  imports: [RouterOutlet],
  selector: 'app-root',
  template: `
    <h1>test</h1>
    <router-outlet/>
  `,
})
export class App {
}
