# -*- coding: utf-8 -*-

from django import template
register = template.Library()


@register.filter()
def is_producteur(user):
    if user.is_authenticated() and 'producteurs' in [g['name'] for g in user.groups.values()]:
        return True
    return False
